package io.aext.core.base.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author rojar
 *
 * @date 2021-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ECFindPasswordVO extends ECBaseVO {
	String confirmUrl;
}
