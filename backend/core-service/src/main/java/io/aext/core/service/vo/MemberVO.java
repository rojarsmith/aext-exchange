package io.aext.core.service.vo;

import java.util.Set;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author rojar
 *
 * @date 2021-06-14
 */
@Data
@Accessors(chain = true)
public class MemberVO {
	private Long id;
	private String username;
	private String token;
	private Set<Long> resourceIds;
}
