package io.aext.core.service.model.param;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import io.aext.core.service.model.param.validator.ValidVerifyMethod;
import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-06-26
 */
@Data
public class RegisterParam {
	@NotBlank(message = "{username.null}")
	@Length(min = 3, max = 20, message = "{username.length}")
	String username;

	@NotBlank(message = "{password.null}")
	@Length(min = 6, max = 20, message = "{password.length}")
	String password;

	@NotBlank(message = "{email.null}")
	@Email(message = "{email.format}")
	String email;

	String promotion;

	@NotBlank(message = "{verify.null}")
	String verify;

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
