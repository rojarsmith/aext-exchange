package io.aext.core.base;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.aext.core.base.util.ValueValidate;

/**
 * @author rojar
 *
 * @date 2021-06-06
 */
public class ValueValidateTest {
	@Test
	public void validateUserName() {
		assertTrue(ValueValidate.validateUserName("asdfz_012349"));
		assertFalse(ValueValidate.validateUserName("asdf1A$1zol"));
	}

	@Test
	public void validateEmail() {
		assertTrue(ValueValidate.validateEmail("rojar@edt.com.tw"));
		assertFalse(ValueValidate.validateEmail(" testmail@mail.com"));
		assertFalse(ValueValidate.validateEmail("testm ail@mail.com"));
	}

	@Test
	public void validatePassword() {
		assertTrue(ValueValidate.validatePassword("^@},hJu>[4Bo7TGX"));
		assertFalse(ValueValidate.validatePassword("12345678 aA@"));
		assertFalse(ValueValidate.validatePassword("12345678aA@ "));
		assertFalse(ValueValidate.validatePassword(" 12345678aA@"));
	}
}
