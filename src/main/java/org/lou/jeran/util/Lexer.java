package org.lou.jeran.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple lexer that alternatively matches a sequence of regex
 *
 * @author phuc
 */
public class Lexer {

    /**
     * Returns the matches found by alternating matching each pattern.
     */
    public static Match scan(CharSequence s, Iterable<Pattern> patterns) {
        List<Token> matches = new ArrayList<>();
        do {
            Token m = lookingAt(s, patterns);
            if (m == null) {
                break;
            } else {
                matches.add(m);
                s = s.subSequence(m.text.length(), s.length());
            }
        } while (true);
        return new Match(matches, s);
    }

    /**
     * Finds a match in s for one of the patterns. The match is looked for at
     * the beginning of s.
     */
    private static Token lookingAt(CharSequence s, Iterable<Pattern> patterns) {
        for (Pattern p : patterns) {
            Matcher m = p.matcher(s);
            if (m.lookingAt()) {
                CharSequence match = s.subSequence(0, m.end());
                return new Token(match, p);
            }
        }
        return null;
    }

    /**
     * A single result from matching
     */
    public static class Token {
        /** The matched sequence */
        public final CharSequence text;

        /** The pattern used to match */
        public final Pattern pattern;

        public Token(CharSequence text, Pattern pattern) {
            this.text = text;
            this.pattern = pattern;
        }

        @Override
        public String toString() {
            return text.toString();
        }
    }

    /**
     * All results from matching
     */
    public static class Match {
        /** Result of matching */
        public final List<Token> tokens;

        /** Remaining text not matched */
        public final CharSequence remaining;

        public Match(List<Token> tokens, CharSequence remaining) {
            this.tokens = tokens;
            this.remaining = remaining;
        }

        @Override
        public String toString() {
            return Arrays.asList(tokens, remaining).toString();
        }
    }

}
