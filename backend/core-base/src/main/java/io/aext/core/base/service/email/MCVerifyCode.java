package io.aext.core.base.service.email;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author rojar
 *
 * @date 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class MCVerifyCode extends MCBase {
	String code;
}
