package io.aext.core.service.model.param.validator;

import java.beans.PropertyDescriptor;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * @author rojar
 *
 * @date 2021-06-27
 */
public class ValidMethodValidator implements ConstraintValidator<ValidMethod, Object> {

	String usernameFieldName;
	String emailFieldName;
	String methodFieldName;
		
	@Override
	public void initialize(ValidMethod constraintAnnotation) {
		usernameFieldName = "username";
		emailFieldName = "email";
		methodFieldName = "method";
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		try {
			PropertyDescriptor pdUsername = BeanUtils.getPropertyDescriptor(value.getClass(), usernameFieldName);
			PropertyDescriptor pdEmail = BeanUtils.getPropertyDescriptor(value.getClass(), emailFieldName);
			PropertyDescriptor pdMethod = BeanUtils.getPropertyDescriptor(value.getClass(), methodFieldName);
			String username = (String)pdUsername.getReadMethod().invoke(value);
			String email = (String)pdEmail.getReadMethod().invoke(value);
			String method = (String)pdMethod.getReadMethod().invoke(value);
	
			if (method.toUpperCase().equals("EMAIL")) {
				if(StringUtils.hasText(method)) {
				    return true;
				}
			}else if(method.toUpperCase().equals("SMS")) {
				if(StringUtils.hasText(method)) {
				    return true;
				}
			}

			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
