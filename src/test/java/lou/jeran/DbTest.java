package lou.jeran;

import org.junit.Test;

import lou.jeran.db.Db;
import lou.jeran.db.H2Db;
import lou.jeran.util.IO;

/**
 * Since the 'production' db is so small, can test it directly.
 *
 * @author phuc
 */
public class DbTest extends TestBase {

    @Test
    public void canRunQuery() throws Exception {
        String sql = IO.readFromClasspath("/reset_db_h2.sql");
        Db db = new H2Db("TENNIS", sql);

        db.query("SELECT * FROM PLAYERS LIMIT 5", rs -> {
            assertEquals("Column count changed!",
                12, rs.getMetaData().getColumnCount());
            return rs;
        });
    }

}
