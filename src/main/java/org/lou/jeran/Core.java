package org.lou.jeran;

import static java.util.Arrays.asList;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.lou.jeran.util.Util;

/**
 * Core logic which determines what the world is going to look like after
 * processing the request.
 * <p>
 * TODO decouple from Db, should only work with domain concepts like Table
 *
 * @author Phuc
 */
public class Core {

    public static String response(
        String path,
        Map<String, String[]> parms,
        Db db,
        View view)
        throws SQLException
    {
        if (path.equals("")) {
            return view.html(queryTables(db));
        }
        else if (path.equals("run") && parms.containsKey("sql")) {
            return run(parms.get("sql"), db, view);
        }
        else {
            String msg = String.format("Unrecognized path/parms %s %s", path,
                parms);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Show relevant tables
     */
    private static List<Table> queryTables(Db db) {
        try {
            List<String> tableNames = db.findTableNames();
            return queryTables(db, tableNames);
        } catch (Exception e) {
            return asList(errorTable(e));
        }
    }

    /**
     * Query tables given their names.
     */
    private static List<Table> queryTables(Db db, List<String> tableNames) {
        List<Table> tables = new ArrayList<>();
        for (String name : tableNames) {
            String select = String.format("SELECT * FROM %s", name);
            Table table = tryQuery(db, select);
            table = table.rename(name);
            tables.add(table);
        }
        return tables;
    }

    private static String run(String[] sqls, Db db, View view)
        throws SQLException
    {
        if (sqls.length == 0 || Util.isBlank(sqls[0])) {
            return "";
        }
        else {
            return view.table(tryQuery(db, sqls[0]));
        }
    }

    /**
     * Run query to get result table or error table. TODO check sql for syntax
     * error. TODO show result-set metadata.
     */
    private static Table tryQuery(Db db, String sql) {
        try {
            return db.query(sql, Core::toTable);
        } catch (SQLException e) {
            return errorTable(e);
        }
    }

    private static Table errorTable(Exception e) {
        return new Table(
            "Error", asList("Error"), asList(asList(e.getMessage())));
    }

    /**
     * Consume entirely a result-set to make a table structure. TODO convert
     * loops to functional reduction
     */
    private static Table toTable(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int size = meta.getColumnCount();

        Set<String> tableNames = new LinkedHashSet<>();
        for (int i = 1; i <= size; i++) {
            tableNames.add(meta.getTableName(i));
        }
        String tableName = tableNames.stream().collect(Collectors.joining("_"));

        List<String> header = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            header.add(meta.getColumnLabel(i));
        }

        List<List<Object>> rows = new ArrayList<>();
        while (rs.next()) {
            List<Object> row = new ArrayList<>();
            for (int i = 1; i <= size; i++) {
                row.add(rs.getObject(i));
            }
            rows.add(row);
        }

        return new Table(tableName, header, rows);
    }

}
