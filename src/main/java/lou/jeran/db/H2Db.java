package lou.jeran.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * H2 variant of Db.
 *
 * @author Phuc
 */
public class H2Db extends Db {

    /**
     * Create an in-mem H2 db given a script for initializing data.
     */
    public H2Db(String name, String initScript) throws Exception {
        super(name, new org.h2.Driver(), getUrl(name), "", "");

        // init the db by executing a script
        List<String> stmts = SqlLexer.splitStatements(initScript);
        execStmtsAsUnit(stmts);
    }

    private static String getUrl(String dbName) {
        return String.format("jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1", dbName);
    }

    @Override
    public List<String> findTableNames() throws Exception {
        String forTableNames = getQueryForTableNames(getName());
        return query(forTableNames, (ResultSet rs) -> {
            ArrayList<String> names = new ArrayList<>();
            while (rs.next())
                names.add(rs.getString("TABLE_NAME"));
            return names;
        });
    }

    private static String getQueryForTableNames(String dbName) {
        StringJoiner s = new StringJoiner("\n");
        s.add("SELECT TABLE_NAME");
        s.add("FROM INFORMATION_SCHEMA.TABLES");
        s.add("WHERE TABLE_CATALOG = '" + dbName + "'");
        s.add("  AND TABLE_SCHEMA = 'PUBLIC'");
        s.add("  AND TABLE_TYPE = 'TABLE'");
        return s.toString();
    }

}