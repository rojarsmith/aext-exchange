package io.aext.core.service.model.param;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.util.StringUtils;

import io.aext.core.service.model.param.validator.ValidMethod;
import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-6-26
 */
@Data
@ValidMethod(message = "Mehtod not correct")
public class PasswordForgetParam {
	@Length(min = 3, max = 20, message = "{Register.username.length}")
	private String username;

	@Email(message = "{Register.email.format}")
	private String email;

	/*
	 * SMS, EMAIL
	 */
	@NotBlank
	private String method;

	public boolean isMethodEmail() {
		return this.method.toUpperCase().equals("EMAIL");
	}

	public boolean isMethodSMS() {
		return this.method.toUpperCase().equals("SMS");
	}

	public boolean isMethodValid() {
		return isMethodEmail() || isMethodSMS();
	}

	public static boolean checkUsernameAndEmail(String username, String email) {
		if (StringUtils.hasText(username) || StringUtils.hasText(email)) {
			return true;
		}
		return false;
	}
}
