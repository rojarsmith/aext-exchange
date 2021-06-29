package io.aext.core.service.model.param.validator;

import java.beans.PropertyDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanUtils;

import io.aext.core.base.util.ValueValidate;

/**
 * @author rojar
 *
 * @date 2021-06-27
 */
public class ValidIdentityFeaturesValidator implements ConstraintValidator<ValidIdentityFeatures, Object> {

	String identityFeaturesFieldName;

	@Override
	public void initialize(ValidIdentityFeatures constraintAnnotation) {
		identityFeaturesFieldName = constraintAnnotation.identityFeaturesFieldName();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		try {
			PropertyDescriptor pdIdentityFeatures = BeanUtils.getPropertyDescriptor(value.getClass(),
					identityFeaturesFieldName);
			String identityFeatures = (String) pdIdentityFeatures.getReadMethod().invoke(value);

			boolean v = ValueValidate.validateOnlyAscii(identityFeatures);

			return v;
		} catch (Exception e) {
			return false;
		}
	}

}
