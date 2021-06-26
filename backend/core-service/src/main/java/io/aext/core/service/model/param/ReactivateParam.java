package io.aext.core.service.model.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-06-26
 */
@Data
public class ReactivateParam {
	@NotBlank
	String method;

	public boolean isMethodEmail() {
		return this.method.toUpperCase().equals("EMAIL");
	}

	public boolean isMethodSMS() {
		return this.method.toUpperCase().equals("SMS");
	}

	public boolean isMethodValid() {
		return isMethodEmail() || isMethodSMS();
	}
}
