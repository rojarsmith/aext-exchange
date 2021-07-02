package io.aext.core.base.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author rojar
 *
 * @date 2021-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ECActiveConfirmVO extends ECBaseVO {
	String confirmUrl;
}
