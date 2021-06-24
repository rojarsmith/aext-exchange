package io.aext.core.base.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.Instant;

/**
 * @author rojar
 *
 * @date 2021-06-06
 */
public class SHA2 {
	public static String getSHA256(String input) {

		String toReturn = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			digest.update(input.getBytes("utf8"));
			toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return toReturn;
	}

	public static String getSHA512(String input) {

		String toReturn = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.reset();
			digest.update(input.getBytes("utf8"));
			toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return toReturn;
	}
	
	public static String getSHA256Short(String input, int beginIndex, int endIndex) {
		return getSHA256(input).substring(beginIndex, endIndex);
	}

	public static String getSHA512Short(String input, int beginIndex, int endIndex) {
		return getSHA512(input).substring(beginIndex, endIndex);
	}

	public static String getSHA512ShortByNow(int beginIndex, int endIndex) {
		return getSHA512(Instant.now().toString()).substring(beginIndex, endIndex);
	}

	public static String getSHA256VerifyLen6() {
		int v = getSHA256(Instant.now().toString()).hashCode() % 1000000;
		v = v < 0 ? v * -1 : v;

		String code = Integer.toString(v);
		if (code.length() != 6) {
			return "831226";
		}
		return code;
	}
}
