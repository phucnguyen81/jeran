package org.lou.jeran.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.lou.jeran.Db;
import org.lou.jeran.View;

/**
 * Create/release resources global to the app.
 * <p>
 * TODO externalize database parameters. TODO use server log
 *
 * @author Phuc
 */
@WebListener
public class ContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		ctx.setAttribute(View.class.getName(), new View());
		try {
			Db db = new Db("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/TENNIS?useSSL=false", "BOOKSQL",
					"BOOKSQLPWD");
			ctx.setAttribute(Db.class.getName(), db);
		} catch (Exception e) {
			throw new AssertionError("Failed to connect to database", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		Object view = ctx.getAttribute(View.class.getName());
		if (view == null) {
			ctx.log("Required template attribute not found: " + View.class.getName());
		}
		Object db = ctx.getAttribute(Db.class.getName());
		if (db == null) {
			ctx.log("Required database not found: " + Db.class.getName());
		}
	}

}
