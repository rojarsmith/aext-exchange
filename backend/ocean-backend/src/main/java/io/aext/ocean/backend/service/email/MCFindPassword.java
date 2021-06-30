package io.aext.ocean.backend.service.email;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MCFindPassword extends MCBase {
	String confirmUrl;
}
