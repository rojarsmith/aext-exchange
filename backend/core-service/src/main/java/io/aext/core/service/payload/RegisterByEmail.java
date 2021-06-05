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
	@NotBlank(message = "{RegisterByEmail.email.null}")
	@Email(message = "{RegisterByEmail.email.format}")
	private String email;

	@NotBlank(message = "{RegisterByEmail.password.null}")
	@Length(min = 6, max = 20, message = "{RegisterByEmail.password.length}")
	private String password;

	@NotBlank(message = "{RegisterByEmail.username.null}")
	@Length(min = 3, max = 20, message = "{RegisterByEmail.username.length}")
	private String username;

	@NotBlank(message = "{RegisterByEmail.country.null}")
	private String country;

	private String promotion;

	@NotBlank(message = "{RegisterByEmail.verify.null}")
	private String verify;
}
