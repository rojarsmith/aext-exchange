package io.aext.ocean.backend.model.param.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author rojar
 *
 * @date 2021-06-28
 */
@Constraint(validatedBy = ValidIdentityFeaturesValidator.class)
@Target({ TYPE }) // Class-level constraints
@Retention(RUNTIME)
@Documented
public @interface ValidIdentityFeatures {
	String message() default "Identity Features not valid.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String identityFeaturesFieldName();
}
