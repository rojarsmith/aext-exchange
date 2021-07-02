package io.aext.core.base.model.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author Rojar Smith
 *
 * @date 2021-07-02
 */
@Data
public class ValidResultVO {
	Map<String, List<Map<String, String>>> data;
	List<Map<String, String>> errors;

	public ValidResultVO() {
		this.data = new HashMap<>();
		this.errors = new ArrayList<>();
		this.data.put("errors", errors);
	}

	public void add(String type, String name, String message) {
		Map<String, String> error = new HashMap<>();
		error.put("type", type);
		error.put("name", name);
		error.put("message", message);
		this.errors.add(error);
		this.data.put("errors", errors);
	}

	public boolean isError() {
		if (errors.size() > 0) {
			return true;
		}

		return false;
	}
}
