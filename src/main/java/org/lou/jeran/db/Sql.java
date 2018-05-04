package org.lou.jeran.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Facade over java.sql package. Most import-statements should be from java.sql.
 *
 * @author phuc
 */
public class Sql {

    /**
     * Execute sql-statements as a unit, i.e. commit if all succeed, rollback on
     * error
     */
    public static void execStmtsAsUnit(
        Connection conn, Iterable<String> stmts)
        throws Exception
    {
        boolean autoCommit = conn.getAutoCommit();
        try (Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);
            for (String s : stmts) {
                stmt.execute(s);
            }
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new Exception(
                "Connection rollbacked on exception: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(autoCommit);
        }
    }

    public static Connection connect(
        String url, String user, String password)
        throws Exception
    {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Directly load driver by registering an instance.
     */
    public static void register(Driver driver) throws Exception {
        DriverManager.registerDriver(driver);
    }

}
