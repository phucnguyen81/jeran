package lou.jeran.util;

import java.util.List;

import org.junit.Test;

import lou.jeran.TestBase;
import lou.jeran.db.SqlLexer;
import lou.jeran.util.IO;

public class SqlTest extends TestBase {

    @Test
    public void splitStatements() throws Exception {
        String sql = "select * from A;\nselect * from B";
        List<String> stmts = SqlLexer.splitStatements(sql);
        assertEquals(asList("select * from A", "\nselect * from B"), stmts);
    }

    @Test
    public void ignoreComments() throws Exception {
        String sql = "select * from A;-- a select \nselect * from B";
        List<String> stmts = SqlLexer.splitStatements(sql);
        assertEquals(asList("select * from A", "\nselect * from B"), stmts);
    }

    @Test
    public void ignoreBlockComment() throws Exception {
        String sql = "select * from A; /* a select */";
        List<String> stmts = SqlLexer.splitStatements(sql);
        assertEquals(asList("select * from A"), stmts);
    }

    @Test
    public void recognizeDash() throws Exception {
        String sql = "select * from A where B - C > 0";
        List<String> stmts = SqlLexer.splitStatements(sql);
        assertEquals(asList(sql), stmts);
    }

    @Test
    public void recognizeLiteral() throws Exception {
        String sql = "select * from A where B = ';'";
        List<String> stmts = SqlLexer.splitStatements(sql);
        assertEquals(asList(sql), stmts);
    }

    /**
     * Split on whole file, simply print to console
     */
    @Test
    public void splitWholeFile() throws Exception {
        String sql = IO.readFromClasspath("/reset_db_h2.sql");
        for (String stmt : SqlLexer.splitStatements(sql)) {
            System.out.print(stmt);
            System.out.print(";");
        }
    }

}
