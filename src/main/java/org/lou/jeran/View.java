package org.lou.jeran;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.List;

import org.lou.jeran.util.IO;
import org.lou.jeran.util.Util;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 * Render domain data to html.
 * <p>
 * This is thread-safe assumming that STGroupFile is thread-safe, which it
 * should be.
 *
 * @author Phuc
 */
public class View {

    private final STGroupFile stg;
    private final String exercies;

    public View() throws IOException {
        this.stg = new STGroupFile("view.stg");
        String markdown = IO.readFromClasspath("/exercies.md");
        this.exercies = Util.fromMarkdownToHtml(markdown);
    }

    public String html(List<Table> tables) {
        tables = noNulls(tables);
        return get("html").add("tables", tables)
            .add("exercies", exercies).render();
    }

    public String table(Table table) {
        table = noNulls(table);
        return get("table").add("table", table).render();
    }

    private static List<Table> noNulls(List<Table> tables) {
        return tables.stream().map(View::noNulls).collect(toList());
    }

    /**
     * Make new table by replacing nulls with empty-strings. This is needed
     * since nulls are not included when applying templates.
     */
    private static Table noNulls(Table t) {
        List<List<Object>> rows = t.rows.stream().map(View::noNullCols).collect(toList());
        return new Table(t.name, t.header, rows);
    }

    /**
     * Make new list by replacing null values with empty-strings
     */
    private static List<Object> noNullCols(List<Object> row) {
        return row.stream().map(col -> col == null ? "" : col).collect(toList());
    }

    private ST get(String name) {
        ST st = stg.getInstanceOf(name);
        if (st == null) {
            throw new IllegalArgumentException(
                    "Found no template named " + name + " in template group " + stg.getName());
        } else {
            return st;
        }
    }

}
