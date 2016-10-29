package org.lou.jeran;

import org.junit.Test;
import org.lou.jeran.Db;

/**
 * TODO use in-memory db
 *
 * @author Phuc
 */
public class DbTest {

	@Test
	public void canRunQuery() throws Exception {
		Db db = new Db("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/TENNIS?useSSL=false", "BOOKSQL", "BOOKSQLPWD");
		Object m = db.query("SELECT * FROM PLAYERS LIMIT 5", rs -> rs.getMetaData());
		System.out.println(m);
	}

}
