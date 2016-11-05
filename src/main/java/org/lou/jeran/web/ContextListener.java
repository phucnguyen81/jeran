package org.lou.jeran.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.lou.jeran.Db;
import org.lou.jeran.View;
import org.lou.jeran.util.IO;

/**
 * Create/release resources global to the app.
 * <p>
 * TODO use server log for logging?
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
			// loads the script to init db
			String sql = IO.readFromClasspath("/reset_db_h2.sql");
			Db db = Db.h2("TENNIS", sql);

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
			ctx.log("", new IllegalStateException("Required template not found: " + View.class.getName()));
		}
		Object db = ctx.getAttribute(Db.class.getName());
		if (db == null) {
			ctx.log("", new IllegalStateException("Required database not found: " + Db.class.getName()));
		}
	}

}
