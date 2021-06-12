package io.aext.core.base.service.email;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author rojar
 *
 * @date 2021-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MCActiveConfirm extends MCBase {
	String confirmUrl;
}
