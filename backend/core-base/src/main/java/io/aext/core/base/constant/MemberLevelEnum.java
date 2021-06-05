package io.aext.core.base.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import io.aext.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rojar
 *
 * @date 2021-06-05
 */
@AllArgsConstructor
@Getter
public enum MemberLevelEnum implements BaseEnum {
	GENERAL("General"), VERIFIED1("Verified1");

	@Setter
	private String name;
	
	@Override
	@JsonValue
	public int getOrdinal() {
		return this.ordinal();
	}

}
