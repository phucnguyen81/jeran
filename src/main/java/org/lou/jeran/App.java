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

import org.lou.jeran.db.Db;
import org.lou.jeran.util.Util;

/**
 * Directly handles user requests.
 *
 * @author Phuc
 */
public class App {

    private final Db db;
    private final View view;

    public App(Db db, View view) {
        this.db = db;
        this.view = view;
    }

    /**
     * Handles a request given its name and parameters. Returns the result as an
     * html string.
     */
    public String handle(String name, Map<String, String[]> parms)
        throws SQLException
    {
        if (name.equals(""))
            return view.html(queryTables());
        else if (name.equals("run") && parms.containsKey("sql"))
            return run(parms.get("sql"));
        else
            throw new IllegalArgumentException(String.format(
                "Unrecognized path/parms %s %s", name, parms));
    }

    /**
     * Show relevant tables
     */
    private List<Table> queryTables() {
        try {
            List<String> tableNames = db.findTableNames();
            return queryTables(tableNames);
        } catch (Exception e) {
            return asList(errorTable(e));
        }
    }

    /**
     * Query tables given their names.
     */
    private List<Table> queryTables(List<String> tableNames) {
        List<Table> tables = new ArrayList<>();
        for (String name : tableNames) {
            String select = String.format("SELECT * FROM %s", name);
            Table table = tryQuery(select);
            table = table.rename(name);
            tables.add(table);
        }
        return tables;
    }

    private String run(String[] sqls) throws SQLException {
        if (sqls.length == 0 || Util.isBlank(sqls[0]))
            return "";
        else
            return view.table(tryQuery(sqls[0]));
    }

    /**
     * Run query to get result table or error table. TODO check sql for syntax
     * error.
     */
    private Table tryQuery(String sql) {
        try {
            return db.query(sql, App::toTable);
        } catch (Exception e) {
            return errorTable(e);
        }
    }

    private static Table errorTable(Exception e) {
        return new Table(
            "Error", asList("Error"), asList(asList(e.getMessage())));
    }

    /**
     * Consume entirely a result-set to make a table structure. TODO convert
     * loops to functional reduction. TODO show result-set metadata.
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
