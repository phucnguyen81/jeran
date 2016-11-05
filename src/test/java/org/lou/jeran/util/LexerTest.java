package org.lou.jeran.util;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;
import org.lou.jeran.util.Lexer;
import org.lou.jeran.util.Lexer.Match;

public class LexerTest {

	@Test
	public void singlePattern1() throws Exception {
		List<Pattern> patterns = asList(Pattern.compile("ab"));
		Match match = Lexer.scan("ab", patterns);
		assertEquals(asList("ab"), tokenStrings(match));
		assertEquals("", match.remaining);
	}

	@Test
	public void singlePattern2() throws Exception {
		List<Pattern> patterns = asList(Pattern.compile("ab"));
		Match match = Lexer.scan("abc", patterns);
		assertEquals(asList("ab"), tokenStrings(match));
		assertEquals("c", match.remaining);
	}

	@Test
	public void singlePattern3() throws Exception {
		List<Pattern> patterns = asList(Pattern.compile("ab"));
		Match match = Lexer.scan("abab", patterns);
		assertEquals(asList("ab", "ab"), tokenStrings(match));
		assertEquals("", match.remaining);
	}

	@Test
	public void multiplePatterns() throws Exception {
		List<Pattern> patterns = Arrays.asList(Pattern.compile("ab"), Pattern.compile("c"));
		Match match = Lexer.scan("abcabd", patterns);
		assertEquals(asList("ab", "c", "ab"), tokenStrings(match));
		assertEquals("d", match.remaining);
	}

	/**
	 * Extract the matched strings from tokens
	 */
	private static List<CharSequence> tokenStrings(Match m) {
		List<CharSequence> tokens = new ArrayList<>();
		m.tokens.forEach(t -> tokens.add(t.text));
		return tokens;
	}
}
