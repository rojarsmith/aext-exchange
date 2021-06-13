package io.aext.core.base;

import java.util.EnumSet;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.aext.core.base.constant.MemberLevelEnum;

/**
 * @author rojar
 *
 * @date 2021-06-13
 */
@Converter(autoApply = true)
public class MemberLevelEnumSetConverter extends EnumSetConverter<MemberLevelEnum>
		implements AttributeConverter<EnumSet<MemberLevelEnum>, String> {
	public MemberLevelEnumSetConverter() {
		super(MemberLevelEnum.class);
	}
}
