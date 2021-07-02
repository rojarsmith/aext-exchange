package io.aext.core.base.service;

/**
 * @author Rojar Smith
 *
 * @date 2021-07-02
 */
public interface LocaleMessageSourceService {
	String getMessage(String code);

	/**
	 *
	 * @param code ： messages key.
	 * @param args : arguments array.
	 * @return
	 */
	String getMessage(String code, Object[] args);

	/**
	 *
	 * @param code           : messages key.
	 * @param args           : arguments array.
	 * @param defaultMessage : default message without key.
	 * @return
	 */
	String getMessage(String code, Object[] args, String defaultMessage);
}
