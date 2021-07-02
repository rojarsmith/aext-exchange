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
public class ActivateParam {
	@NotBlank(message = "{username.null}")
	@Length(min = 3, max = 20, message = "{username.length}")
	String username;

	@NotBlank(message = "{generic.invalid}")
	String token;
}
