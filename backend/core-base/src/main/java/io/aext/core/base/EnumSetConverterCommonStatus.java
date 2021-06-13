package io.aext.core.base;

import java.util.EnumSet;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.aext.core.base.constant.CommonStatus;

/**
 * @author rojar
 *
 * @date 2021-06-13
 */
@Converter(autoApply = true)
public class EnumSetConverterCommonStatus extends EnumSetConverter<CommonStatus>
		implements AttributeConverter<EnumSet<CommonStatus>, String> {
	public EnumSetConverterCommonStatus() {
		super(CommonStatus.class);
	}
}
