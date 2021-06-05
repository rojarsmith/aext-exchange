package io.aext.core.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aext.core.base.payload.MessageResponse;

public class BaseController {
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	protected MessageResponse success() {
		return MessageResponse.success();
	}

	protected MessageResponse success(String msg) {
		return MessageResponse.success(msg);
	}

	protected MessageResponse success(String msg, Object obj) {
		return MessageResponse.success(msg, obj);
	}

	protected MessageResponse error(String msg) {
		return MessageResponse.error(msg);
	}

	protected MessageResponse error(int code, String msg) {
		return MessageResponse.error(code, msg);
	}
}
