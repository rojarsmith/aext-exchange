package io.aext.core.base.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Rojar Smith
 *
 * @date 2021-07-01
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitedAccess {
	/**
	 * Max call function times in period, default 60.
	 * 
	 * @return
	 */
	long frequency() default 60;

	/**
	 * Period time, default 1 min
	 * 
	 * @return
	 */
	long second() default 60;

	/**
	 * Times touch period limit in heavy period, default 10.
	 * 
	 * @return
	 */
	long heavyFrequency() default 10;

	/**
	 * Heavy period time, default 10 min
	 * 
	 * @return
	 */
	long heavySecond() default 60 * 10;

	/**
	 * Ban time after touching heavy limit, default 1 day
	 * 
	 * @return
	 */
	long heavyDelay() default 60 * 60 * 24;
}
