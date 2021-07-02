package io.aext.core.service.model.param;

import javax.validation.constraints.NotBlank;

import io.aext.core.service.model.param.validator.ValidVerifyMethod;
import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-07-02
 */
@Data
public class ReactivateParam {
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
