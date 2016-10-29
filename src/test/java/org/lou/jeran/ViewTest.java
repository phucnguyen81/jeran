package org.lou.jeran;

import static org.junit.Assert.assertTrue;

import static java.util.Arrays.asList;
import java.util.List;

import org.junit.Test;
import org.lou.jeran.Table;
import org.lou.jeran.View;

/**
 * Test pieces of templates that should not change often.
 *
 * @author Phuc
 */
public class ViewTest {

	@Test
	public void constructor() {
		new View();
	}

	@Test
	public void table() {
		List<String> header = asList("A", "B");
		List<Object> row1 = asList("a1", "b1");
		List<Object> row2 = asList("a2", "b2");
		List<List<Object>> rows = asList(row1, row2);

		Table t = new Table("TEST", header, rows);
		String s = new View().table(t);

		assertTrue("Expect table end tag", s.contains("</table>"));
		assertTrue("Expect row end tag", s.contains("</tr>"));
		assertTrue("Exepect content of row1", s.contains("a1"));
		assertTrue("Exepect content of row2", s.contains("b2"));
	}

}
