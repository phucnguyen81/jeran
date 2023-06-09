package lou.jeran.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lou.jeran.App;

/**
 * Front Controller translates http-requests to app-requests then dispatches
 * them to {@link App}.
 *
 * @author Phuc
 */
@SuppressWarnings("serial")
@WebServlet(
    asyncSupported = false,
    name = "FrontServlet",
    urlPatterns = { "/app/*" })
public final class FrontServlet extends HttpServlet {

    @Override
    public String getServletInfo() {
        return "@Phuc 2016";
    }

    @Override
    protected void doGet(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException
    {
        process(request, response);
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException
    {
        process(request, response);
    }

    private void process(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html;charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.write(response(request));
        }
    }

    private String response(HttpServletRequest req) {
        String path = getPath(req);
        Map<String, String[]> parms = req.getParameterMap();
        try {
            App app = getContextAttr(App.class);
            return app.handle(path, parms);
        } catch (IllegalStateException | IllegalArgumentException ise) {
            return errorMessage("Application error", ise);
        } catch (SQLException sql) {
            return errorMessage("Database error", sql);
        } catch (Throwable th) {
            return errorMessage("Server error", th);
        }
    }

    /**
     * Get the path-info; use empty-string for null and remove the "/"
     */
    private static String getPath(HttpServletRequest req) {
        final String p = req.getPathInfo();
        if (p == null) {
            return "";
        }
        else if (p.startsWith("/")) {
            return p.replaceFirst("/", "");
        }
        else {
            return p;
        }
    }

    /**
     * Retrieve app-services initialized with {@link ContextListener}.
     */
    private <T> T getContextAttr(Class<T> type) {
        Object attr = getServletContext().getAttribute(type.getName());
        if (attr == null) {
            String msg = String.format("Resource type %s not initialized",
                type.getName());
            throw new IllegalStateException(msg);
        }
        else {
            return type.cast(attr);
        }
    }

    private String errorMessage(String type, Throwable cause) {
        String msg = String.format("%s: %s", type, cause.getMessage());
        log(msg, cause);
        return msg;
    }

}