package io.aext.core.base.service.email;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author rojar
 *
 * @date 2021-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MCFindPassword extends MCBase {
	String confirmUrl;
}
