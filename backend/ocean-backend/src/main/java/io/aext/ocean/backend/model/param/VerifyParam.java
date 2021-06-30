package io.aext.ocean.backend.model.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-06-26
 */
@Data
public class VerifyParam {
	@NotBlank
	String type;

	/*
	 * SMS, EMAIL
	 */
	@NotBlank
	String method;
}
