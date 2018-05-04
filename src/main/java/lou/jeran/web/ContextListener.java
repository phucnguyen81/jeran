package lou.jeran.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import lou.jeran.App;
import lou.jeran.View;
import lou.jeran.db.H2Db;
import lou.jeran.util.IO;

/**
 * Create/release resources global to the app.
 * <p>
 * TODO inject server log into App?
 *
 * @author Phuc
 */
@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        ctx.setAttribute(App.class.getName(), createApp());
    }

    private static App createApp() {
        try {
            String sql = IO.readFromClasspath("/reset_db_h2.sql");
            return new App(new H2Db("TENNIS", sql), new View());
        } catch (Exception e) {
            throw new AssertionError("Failed to create App", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        Object app = ctx.getAttribute(App.class.getName());
        if (app == null) {
            ctx.log("", new IllegalStateException(
                "Required app not found: " + App.class.getName()));
        }
    }

}
