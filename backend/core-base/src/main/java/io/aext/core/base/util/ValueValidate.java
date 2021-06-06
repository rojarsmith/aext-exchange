package io.aext.core.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rojar
 *
 * @date 2021-06-06
 */
public class ValueValidate {
	/*
	 * Validate user name.
	 * 
	 * 5-21 digits. Can only be letters (case sensitive), numbers, and underscores.
	 * Cannot start and end with underscores.
	 */
	public static boolean validateUserName(String target) {
		Pattern pattern = Pattern.compile("^[a-zA-Z\\d]\\w{3,19}[a-zA-Z\\d]$");
		Matcher matcher = pattern.matcher(target);
		return matcher.find();
	}

	/*
	 * Validate email.
	 * 
	 * Couldn't start or finish with a dot Shouldn't contain spaces into the string
	 * Shouldn't contain special chars (<:, *,ecc) Could contain dots in the middle
	 * of mail address before the @ Could contain a double doman ( '.de.org' or
	 * similar rarity)
	 */
	public static boolean validateEmail(String target) {
		Pattern pattern = Pattern.compile("^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$");
		Matcher matcher = pattern.matcher(target);
		return matcher.find();
	}

	/*
	 * Validate password.
	 * 
	 * Must 8-16 characters with no space
	 */
	public static boolean validatePassword(String target) {
		Pattern pattern = Pattern.compile("^([^\\s]){8,16}$");
		Matcher matcher = pattern.matcher(target);
		return matcher.find();
	}
}
