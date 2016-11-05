package org.lou.jeran.util;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.lou.jeran.util.Lexer.Token;

/**
 * Support for handling sql
 *
 * @author phuc
 */
public class Sql {

	private static final Pattern LINE_COMMENT = Pattern.compile("--.*?$", Pattern.MULTILINE);
	private static final Pattern BLOCK_COMMENT = Pattern.compile("/\\*.+?\\*/", Pattern.DOTALL);
	private static final Pattern SEMI = Pattern.compile(";");
	private static final Pattern LITERAL = Pattern.compile("'(?:''|[^'])*'");

	/**
	 * After defining other tokens, code is just what does not contain the
	 * starting characters of other tokens. e.g. code does not contain '--'
	 * which starts line-comment
	 */
	private static final Pattern CODE = Pattern.compile("(?:(?!(?:--|;|/\\*|')).)+", Pattern.DOTALL);

	/**
	 * Token patterns for sql code
	 */
	private static final List<Pattern> PATTERNS = asList(CODE, LINE_COMMENT, BLOCK_COMMENT, LITERAL, SEMI);

	/**
	 * Splits a sql script into sql statements
	 */
	public static List<String> splitStatements(String sql) {
		// get code and semi-colon
		List<Token> tokens = Lexer.scan(sql, PATTERNS).tokens.stream()
				.filter(t -> t.pattern == CODE || t.pattern == LITERAL || t.pattern == SEMI).collect(toList());
		// split on semi-colon
		List<List<Token>> code = Util.split(tokens, t -> t.pattern == SEMI);
		// join token code to make whole statement
		Stream<String> stmts = code.stream().map(tks -> tks.stream().map(tk -> tk.text).collect(joining()));
		// discard empty stmts
		return stmts.filter(stmt -> !stmt.trim().isEmpty()).collect(toList());
	}
}
