package org.lou.jeran.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Useful static functions/methods.
 *
 * @author phuc
 */
public class Util {

    /**
     * Like splitting a string on a pattern, this splits a sequence on element
     * matching a predicate.
     */
    public static <T> List<List<T>> split(Iterable<T> seq, Predicate<T> pred) {
        List<List<T>> runs = new ArrayList<>();
        List<T> run = new ArrayList<>();
        for (T t : seq) {
            if (pred.test(t) && !run.isEmpty()) {
                runs.add(new ArrayList<>(run));
                run.clear();
            }
            else {
                run.add(t);
            }
        }
        // add the last run if any
        if (!run.isEmpty()) {
            runs.add(run);
        }
        return runs;
    }

    public static String fromMarkdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

}
