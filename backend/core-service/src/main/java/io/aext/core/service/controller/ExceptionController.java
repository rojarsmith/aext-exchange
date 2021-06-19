package io.aext.core.service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import io.aext.core.base.model.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rojar
 *
 * @date 2021-06-11
 */
@Slf4j
@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(value = { ResponseStatusException.class })
	public ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
		log.error("'handleResponseStatusException':", e);
		Map<String, Object> data = new HashMap<>();
		data.put("stackTrace", e.getStackTrace());
		return ResponseEntity.badRequest().body(new ResultVO(e.getReason(), data));
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<?> handleException(Exception e) {
		log.error("'handleException':", e);
		Map<String, Object> data = new HashMap<>();
		data.put("stackTrace", e.getStackTrace());
		return ResponseEntity.badRequest().body(new ResultVO(e.getMessage(), data));
	}
}
