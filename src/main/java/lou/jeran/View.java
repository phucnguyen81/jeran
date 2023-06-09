package lou.jeran;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import lou.jeran.util.IO;
import lou.jeran.util.Util;

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
    private final String exercises;
    private final String examples;

    public View() throws IOException {
        this.stg = new STGroupFile("view.stg");
        String exercisesMarkdown = IO.readFromClasspath("/exercies.md");
        this.exercises = Util.fromMarkdownToHtml(exercisesMarkdown);
        String examplesMarkdown = IO.readFromClasspath("/examples.md");
        this.examples = Util.fromMarkdownToHtml(examplesMarkdown);
    }

    public String html(List<Table> tables) {
        tables = noNulls(tables);
        return get("html").add("tables", tables)
            .add("exercises", exercises).add("examples", examples)
            .render();
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
                String.format(
                    "Found no template named %s in template group %s",
                    name, stg.getName()));
        } else {
            return st;
        }
    }

}
