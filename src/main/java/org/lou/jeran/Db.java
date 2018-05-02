package org.lou.jeran;

import static java.util.Arrays.asList;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.IntFunction;

/**
 * A simple facade over JDBC database connection. This is meant for single user;
 * it creates a new connection for each operation.
 *
 * @author Phuc
 */
public class Db {

    private final String name;
    private final String url;
    private final String user;
    private final String password;

    public Db(
        String name,
        Driver driver,
        String url,
        String user,
        String password
    ) throws Exception {
        loadDriver(driver);
        this.name = name;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the table names that this database use.
     */
    public List<String> findTableNames() throws Exception {
        return asList(
            "PLAYERS", "TEAMS", "MATCHES",
            "PENALTIES", "COMMITTEE_MEMBERS");
    }

    /**
     * Directly load driver by registering an instance.
     */
    private static void loadDriver(Driver driver) throws Exception {
        DriverManager.registerDriver(driver);
    }

    /**
     * Exec a sql statement that might change the db. rf is given to handle
     * affected rows. The sql is typically INSERT, DELETE, UPDATE or DDL
     * statements.
     */
    public <T> T update(String sql, IntFunction<T> rf) throws SQLException {
        try (Connection conn = newConnection();
            Statement stmt = conn.createStatement();) {
            return rf.apply(stmt.executeUpdate(sql));
        }
    }

    public interface RsMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    /**
     * Exec sql query and use mpr to transform the result set. The sql is
     * typically SELECT.
     */
    public <T> T query(String sql, RsMapper<T> mpr) throws SQLException {
        try (Connection conn = newConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);) {
            return mpr.map(rs);
        }
    }

    /**
     * Execute sql-statements as a unit, i.e. commit if all succeed, rollback on
     * error
     */
    public void execStmtsAsUnit(Iterable<String> stmts) throws SQLException {
        try (Connection conn = newConnection()) {
            execStmtsAsUnit(conn, stmts);
        }
    }

    /**
     * Execute sql-statements as a unit, i.e. commit if all succeed, rollback on
     * error
     */
    public static void execStmtsAsUnit(Connection conn, Iterable<String> stmts)
        throws SQLException
    {
        boolean autoCommit = conn.getAutoCommit();
        try (Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);
            for (String s : stmts) {
                stmt.execute(s);
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException(
                "Connection rollbacked on exception: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(autoCommit);
        }
    }

    private Connection newConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}