package org.lou.jeran.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.Test;

/**
 * Usage of common-mark markdown to html.
 *
 * @author phuc
 */
public class CommonMarkTest {

    @Test
    public void renderSimpleMarkdownToHtml() {
        String markdown = "This is *Sparta*";
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);
        assertEquals("<p>This is <em>Sparta</em></p>\n", html);
    }

    @Test
    public void renderExerciesMarkdown() throws Exception {
        String markdown = IO.readFromClasspath("/exercies.md");
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);
        assertTrue(html.contains("<h3>Exercise 5.11</h3>"));
    }

}
