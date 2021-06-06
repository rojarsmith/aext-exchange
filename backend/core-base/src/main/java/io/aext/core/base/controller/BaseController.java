package io.aext.core.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import io.aext.core.base.payload.MessageResponse;

public class BaseController {
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

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
