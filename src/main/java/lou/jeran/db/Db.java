package lou.jeran.db;

import static java.util.Arrays.asList;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
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

    /* The database name. This is called CATALOG in H2 */
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
        Sql.register(driver);
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
     * Exec a sql statement that might change the db. Call the given function to
     * handle affected rows. The sql is typically INSERT, DELETE, UPDATE or DDL
     * statements.
     */
    public <T> T update(String sql, IntFunction<T> onRowCount)
        throws Exception
    {
        try (Connection conn = connect();
            Statement stmt = conn.createStatement();) {
            return onRowCount.apply(stmt.executeUpdate(sql));
        }
    }

    /**
     * Exec sql query and use mpr to transform the result set. The sql is
     * typically SELECT.
     */
    public <T> T query(String sql, RsMapper<T> mpr) throws Exception {
        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);) {
            return mpr.map(rs);
        }
    }

    /**
     * Execute sql-statements as a unit, i.e. commit if all succeed, rollback on
     * error
     */
    public void execStmtsAsUnit(Iterable<String> stmts) throws Exception {
        try (Connection conn = connect()) {
            Sql.execStmtsAsUnit(conn, stmts);
        }
    }

    private Connection connect() throws Exception {
        return Sql.connect(url, user, password);
    }

}