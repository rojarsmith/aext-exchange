package io.aext.ocean.backend.model.param.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author rojar
 *
 * @date 2021-06-29
 */
public class ValidVerifyMethodValidator implements ConstraintValidator<ValidVerifyMethod, String> {
	@Override
	public void initialize(ValidVerifyMethod validVerifyMethod) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value.toUpperCase().equals("EMAIL")
				//
				|| value.toUpperCase().equals("SMS")) {
			return true;
		}
		return false;
	}

}
