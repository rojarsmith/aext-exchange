package io.aext.core.base.model.entity.convert;

import java.util.EnumSet;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.aext.core.base.enums.MemberStatus;

/**
 * @author rojar
 *
 * @date 2021-06-13
 */
@Converter(autoApply = true)
public class EnumSetConverterMemberStatus extends EnumSetConverter<MemberStatus>
		implements AttributeConverter<EnumSet<MemberStatus>, String> {
	public EnumSetConverterMemberStatus() {
		super(MemberStatus.class);
	}
}
