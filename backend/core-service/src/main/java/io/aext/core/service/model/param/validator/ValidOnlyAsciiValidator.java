package io.aext.core.service.model.param.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author rojar
 *
 * @date 2021-06-28
 */
public class ValidOnlyAsciiValidator implements ConstraintValidator<ValidOnlyAscii, String> {
	@Override
	public void initialize(ValidOnlyAscii validOnlyAscii) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Pattern pattern = Pattern.compile("^[\\x00-\\x7F]+$");
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}
}
