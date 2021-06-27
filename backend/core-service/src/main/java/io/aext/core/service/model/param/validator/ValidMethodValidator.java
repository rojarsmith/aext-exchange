package io.aext.core.service.model.param.validator;

import java.beans.PropertyDescriptor;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanUtils;

/**
 * @author rojar
 *
 * @date 2021-06-27
 */
public class ValidMethodValidator implements ConstraintValidator<ValidMethod, Object> {

	String methodFieldName;

	@Override
	public void initialize(ValidMethod constraintAnnotation) {
		methodFieldName = "method";
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		try {
			PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(value.getClass(), methodFieldName);
			Optional<Object> rv = Optional.ofNullable(propertyDescriptor.getReadMethod().invoke(value));
			if (rv.isEmpty()) {
				return false;
			}
			String method = (String) rv.get();
			if (method.toUpperCase().equals("EMAIL") || method.toUpperCase().equals("SMS")) {
				return true;
			}

			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
