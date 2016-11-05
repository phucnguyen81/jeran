package org.lou.jeran;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.IntFunction;

import org.lou.jeran.util.Sql;

/**
 * For now, create a new connection for every operation.
 *
 * @author Phuc
 */
public class Db {

    /**
     * Create an in-mem db given a script for initializing data
     */
    public static Db h2(String name, String initScript) throws Exception {
        // use DB_CLOSE_DELAY=-1 to keep the db open
        String url = String.format("jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1", name);
        Db db = new Db(new org.h2.Driver(), url, "", "");

        // init the db by executing a script
        List<String> stmts = Sql.splitStatements(initScript);
        db.execStmtsAsUnit(stmts);

        return db;
    }

    private final String url;
    private final String user;
    private final String password;

    public Db(Driver driver, String url, String user, String password) throws Exception {
        loadDriver(driver);
        this.url = url;
        this.user = user;
        this.password = password;
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
        try (Connection conn = newConnection(); Statement stmt = conn.createStatement();) {
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
    public static void execStmtsAsUnit(Connection conn, Iterable<String> stmts) throws SQLException {
        boolean autoCommit = conn.getAutoCommit();
        try (Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);
            for (String s : stmts) {
                stmt.execute(s);
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Connection rollbacked on exception: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(autoCommit);
        }
    }

    private Connection newConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}