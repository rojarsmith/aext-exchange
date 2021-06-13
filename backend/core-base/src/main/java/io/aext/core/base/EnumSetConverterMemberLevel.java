package io.aext.core.base;

import java.util.EnumSet;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.aext.core.base.constant.MemberLevel;

/**
 * @author rojar
 *
 * @date 2021-06-13
 */
@Converter(autoApply = true)
public class EnumSetConverterMemberLevel extends EnumSetConverter<MemberLevel>
		implements AttributeConverter<EnumSet<MemberLevel>, String> {
	public EnumSetConverterMemberLevel() {
		super(MemberLevel.class);
	}
}
