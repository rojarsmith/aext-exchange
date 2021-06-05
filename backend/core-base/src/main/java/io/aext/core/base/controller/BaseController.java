package io.aext.core.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aext.core.base.payload.MessageResponse;

public class BaseController {
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	protected MessageResponse success() {
		return success("SUCCESS", null);
	}

	protected MessageResponse success(String msg) {
		return success(msg, null);
	}

	protected MessageResponse success(String msg, Object obj) {
		return new MessageResponse(0, msg, obj);
	}

	protected MessageResponse error(String msg) {
		return error(500, msg);
	}

	protected MessageResponse error(int code, String msg) {
		return error(code, msg);
	}

	protected MessageResponse error(int code, String msg, Object obj) {
		return new MessageResponse(code, msg, obj);
	}
}
