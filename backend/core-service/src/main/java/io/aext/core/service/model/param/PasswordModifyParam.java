package io.aext.core.service.model.param;

import javax.validation.constraints.NotBlank;

import io.aext.core.service.model.param.validator.ValidVerifyMethod;
import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-6-29
 */
@Data
public class PasswordModifyParam {
	/*
	 * SMS, EMAIL
	 */
	@NotBlank
	@ValidVerifyMethod(message = "#{InputNotValid}")
	private String verifyMethod;

	public boolean isMethodEmail() {
		return this.verifyMethod.toUpperCase().equals("EMAIL");
	}

	public boolean isMethodSMS() {
		return this.verifyMethod.toUpperCase().equals("SMS");
	}
}
