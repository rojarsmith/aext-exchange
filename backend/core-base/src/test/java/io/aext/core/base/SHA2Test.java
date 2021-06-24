package io.aext.core.base;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.aext.core.base.util.SHA2;

/**
 * @author rojar
 *
 * @date 2021-06-06
 */
public class SHA2Test {
	@Test
	public void commonTest() {
		String s1 = SHA2.getSHA512ShortByNow(0, 16);
		int r1 = s1.hashCode() % 10000;
		assertTrue(10000 > r1);
		String s2 = SHA2.getSHA256VerifyLen6();
		assertTrue(s2.length() == 6);
	}
}
