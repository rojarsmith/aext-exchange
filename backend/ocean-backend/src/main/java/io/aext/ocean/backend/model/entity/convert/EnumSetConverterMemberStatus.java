package io.aext.ocean.backend.model.entity.convert;

import java.util.EnumSet;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.aext.ocean.backend.enums.MemberStatus;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
@Converter(autoApply = true)
public class EnumSetConverterMemberStatus extends EnumSetConverter<MemberStatus>
		implements AttributeConverter<EnumSet<MemberStatus>, String> {
	public EnumSetConverterMemberStatus() {
		super(MemberStatus.class);
	}
}
