package io.aext.core.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import io.aext.core.base.payload.MessageResponse;

public class BaseController {
	protected Map<String, List<Map<String, String>>> buildBindingResultData(BindingResult bindingResult) {
		Map<String, List<Map<String, String>>> data = new HashMap<>();
		List<Map<String, String>> errors = new ArrayList<>();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			Map<String, String> error = new HashMap<>();
			error.put("field", fieldError.getField());
			error.put("message", fieldError.getDefaultMessage());
			errors.add(error);
		}
		data.put("errors", errors);
		return data;
	}

	protected ResponseEntity<?> success() {
		return success("Success", null);
	}

	protected ResponseEntity<?> success(String msg) {
		return success(msg, null);
	}

	protected ResponseEntity<?> success(String msg, Object obj) {
		return ResponseEntity.ok().body(new MessageResponse(msg, obj));
	}

	protected ResponseEntity<?> error(String msg) {
		return error(msg, null);
	}

	protected ResponseEntity<?> error(String msg, Object obj) {
		return ResponseEntity.badRequest().body(new MessageResponse(msg, obj));
	}
}
