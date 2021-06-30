package io.aext.ocean.backend.model.param;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import io.aext.ocean.backend.model.param.validator.ValidOnlyAscii;
import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-6-28
 */
@Data
public class PasswordForgetParam {
	@NotBlank
	@Length(min = 3, max = 50, message = "{Length.SizeRangeNotValid}")
	@ValidOnlyAscii(message = "{ValidIdentityFeatures.InputNotValid}")
	String identityFeatures;

	@NotBlank
	@Length(min = 4, max = 4, message = "{Length.SizeRangeNotValid}")
	String token;
}
