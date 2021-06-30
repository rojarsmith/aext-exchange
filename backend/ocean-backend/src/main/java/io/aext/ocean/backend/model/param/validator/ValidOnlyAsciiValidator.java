package io.aext.ocean.backend.model.param.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import io.aext.ocean.backend.util.ValueValidate;

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
		return ValueValidate.validateOnlyAscii(value);
	}
}
