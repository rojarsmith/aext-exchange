package io.aext.core.service.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-6-05
 */
@Data
public class RegisterByEmail {
	@NotBlank(message = "{LoginByEmail.email.null}")
	@Email(message = "{LoginByEmail.email.format}")
	private String email;

	@NotBlank(message = "{LoginByEmail.password.null}")
	@Length(min = 6, max = 20, message = "{LoginByEmail.password.length}")
	private String password;

	@NotBlank(message = "{LoginByEmail.username.null}")
	@Length(min = 3, max = 20, message = "{LoginByEmail.username.length}")
	private String username;

	@NotBlank(message = "{LoginByEmail.country.null}")
	private String country;

	private String promotion;

	@NotBlank(message = "{LoginByEmail.verify.null}")
	private String verify;
}
