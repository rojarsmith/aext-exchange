package io.aext.ocean.backend.model.entity.convert;

import java.util.EnumSet;
import java.util.Iterator;

import javax.persistence.AttributeConverter;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
public class EnumSetConverter<E extends Enum<E>> implements AttributeConverter<EnumSet<E>, String> {

	private final Class<E> clazz;

	public EnumSetConverter(Class<E> clazz) {
		this.clazz = (Class<E>) clazz;
	}

	@Override
	public String convertToDatabaseColumn(EnumSet<E> attribute) {
		if (attribute == null || attribute.isEmpty()) {
			return null;
		}

		String data = "";
		Iterator<E> i = attribute.iterator();
		while (i.hasNext()) {
			data += i.next().toString() + ",";
		}

		data = data.substring(0, data.length() - 1);

		return data;
	}

	@Override
	public EnumSet<E> convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty()) {
			return null;
		}

		String[] pieces = dbData.split(",");
		if (pieces == null || pieces.length == 0) {
			return null;
		}

		EnumSet<E> data = EnumSet.noneOf(clazz);
		for (int i = 0; i < pieces.length; i++) {
			E item = (E) Enum.valueOf(clazz, pieces[i]);
			data.add(item);
		}

		return data;
	}

}
