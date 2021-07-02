package io.aext.core.service.model.param;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * @author Rojar Smith
 *
 * @date 2021-07-02
 */
@Data
public class LoginParam {
	@NotBlank(message = "{username.null}")
	@Length(min = 3, max = 20, message = "{username.length}")
	private String username;

	@NotBlank(message = "{Register.password.null}")
	@Length(min = 6, max = 20, message = "{password.length}")
	private String password;

	@NotBlank(message = "{verify.null}")
	private String verify;
}
