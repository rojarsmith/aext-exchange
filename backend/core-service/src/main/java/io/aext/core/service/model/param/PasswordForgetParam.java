package io.aext.core.service.model.param;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import io.aext.core.service.model.param.validator.ValidMethod;
import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-6-27
 */
@Data
@ValidMethod(message = "Mehtod not correct")
public class PasswordForgetParam {
	@NotBlank
	@Length(min = 3, max = 50, message = "{Length.SizeRangeNotValid}")
	String search;

	@NotBlank
	@Length(min = 4, max = 4, message = "{Length.SizeRangeNotValid}")
	String token;
}
