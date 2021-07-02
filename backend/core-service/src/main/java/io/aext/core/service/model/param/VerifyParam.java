package io.aext.core.service.model.param;

import javax.validation.constraints.NotBlank;

import io.aext.core.service.model.param.validator.ValidVerifyMethod;
import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-06-26
 */
@Data
public class VerifyParam {
	/*
	 * Function action
	 */
	@NotBlank(message = "{generic.invalid}")
	String action;

	/*
	 * SMS, EMAIL
	 */
	@NotBlank
	@ValidVerifyMethod(message = "{generic.invalid}")
	String verifyMethod;

	public boolean isMethodEmail() {
		return this.verifyMethod.toUpperCase().equals("EMAIL");
	}

	public boolean isMethodSMS() {
		return this.verifyMethod.toUpperCase().equals("SMS");
	}
}
