package io.aext.core.service.model.param.validator;

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
 * @date 2021-06-27
 */
@Constraint(validatedBy = ValidMethodValidator.class)
@Target({ TYPE }) // Class-level constraints
@Retention(RUNTIME)
@Documented
public @interface ValidMethod {
    String message() default "Method not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
