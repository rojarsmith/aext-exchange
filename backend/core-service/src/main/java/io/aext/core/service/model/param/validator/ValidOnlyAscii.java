package io.aext.core.service.model.param.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author rojar
 *
 * @date 2021-06-28
 */
@Constraint(validatedBy = ValidOnlyAsciiValidator.class)
@Target({ METHOD, FIELD }) // Class-level constraints
@Retention(RUNTIME)
@Documented
public @interface ValidOnlyAscii {
	String message() default "Has non ASCII char.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
