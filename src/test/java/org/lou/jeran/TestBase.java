package org.lou.jeran;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

/**
 * Common methods for unit tests
 *
 * @author phuc
 */
public class TestBase extends Assert {

	@SafeVarargs
	public static <T> List<T> asList(T... a) {
		return Arrays.asList(a);
	}

}
