package org.lou.jeran;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.Assert;

/**
 * Common methods for unit tests
 *
 * @author phuc
 */
public class TestBase extends Assert {

	public static <T> Collector<T, ?, List<T>> toList() {
		return Collectors.toList();
	}

	@SafeVarargs
	public static <T> List<T> asList(T... a) {
		return Arrays.asList(a);
	}

}
