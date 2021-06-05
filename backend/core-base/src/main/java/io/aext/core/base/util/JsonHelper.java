package io.aext.core.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
	public static String stringify(Object o) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";
		try {
			json = objectMapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return json;
	}
}
