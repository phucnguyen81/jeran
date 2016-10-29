package org.lou.jeran;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.IntFunction;

/**
 * How the app sees the database.
 * <p>
 * This must be thread-safe. Since Connection is not guaranteed to be
 * thread-safe, for now this would create a new connection for every request.
 * TODO replace DriverMangager with DataSource with connection-pool.
 *
 * @author Phuc
 */
public class Db {

	private final String url;
	private final String user;
	private final String password;

	public Db(String driverClass, String url, String user, String password) throws Exception {
		this(loadDriver(driverClass), url, user, password);
	}

	public Db(Driver driver, String url, String user, String password) throws Exception {
		loadDriver(driver);
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * Load the driver without having it on classpath so that the actual class
	 * can be swapped out or provided at deployment location.
	 * <p>
	 * The newInstance() call is a work-around for noncompliant JVMs.
	 */
	private static Driver loadDriver(String driverClass) throws Exception {
		return (Driver) Class.forName(driverClass).newInstance();
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

	private Connection newConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

}