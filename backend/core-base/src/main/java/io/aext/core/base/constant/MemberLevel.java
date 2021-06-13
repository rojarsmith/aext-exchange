package io.aext.core.base.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import io.aext.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author rojar
 *
 * @date 2021-06-05
 */
@AllArgsConstructor
@Getter
public enum MemberLevel implements BaseEnum {
	REGISTERD, VERIFIED_EMAIL;

	@Override
	@JsonValue
	public int getOrdinal() {
		return this.ordinal();
	}

}
