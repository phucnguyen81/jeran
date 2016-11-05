package org.lou.jeran;

import static java.util.Collections.emptyList;

import java.util.List;

/**
 * Simple table data structure.
 *
 * @author Phuc
 */
public class Table {

    public final String name;
    public final List<String> header;
    public final List<List<Object>> rows;

    /**
     * "Null Table" with empty name, header and rows
     */
    public Table() {
        this("", emptyList(), emptyList());
    }

    public Table(String name, List<String> header, List<List<Object>> rows) {
        this.name = name;
        this.header = header;
        this.rows = rows;
    }

    public Table rename(String newName) {
        return new Table(newName, header, rows);
    }

    @Override
    public String toString() {
        return String.format("Table:%s%s", name, header);
    }

}
