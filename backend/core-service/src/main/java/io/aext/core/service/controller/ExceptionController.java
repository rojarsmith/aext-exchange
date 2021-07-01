package io.aext.core.service.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import io.aext.core.base.controller.BaseController;
import io.aext.core.base.model.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rojar Smith
 *
 * @date 2021-07-01
 */
@Slf4j
@ControllerAdvice
public class ExceptionController extends BaseController {

	@ExceptionHandler(value = { ResponseStatusException.class })
	public ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
		e.setStackTrace(Arrays.stream(e.getStackTrace())
				//
				.filter(ste -> ste.getClassName().startsWith("com.edt.frillback.core"))
				//
				.collect(Collectors.toList())
				//
				.toArray(new StackTraceElement[0]));
		log.error("'handleResponseStatusException':", e);
		Map<String, Object> data = new HashMap<>();
		data.put("stackTrace", e.getStackTrace());
		return ResponseEntity.badRequest().body(new ResultVO<Map<String, Object>>(e.getReason(), data));
	}

	@ExceptionHandler(value = { BindException.class })
	public ResponseEntity<?> handleValidationException(BindException e) {
		log.error("'handleValidationException':", e);
		BindingResult bindingResult = e.getBindingResult();
		Map<String, List<Map<String, String>>> data = buildBindingResultData(bindingResult);
		return error(getMessageML("PARAMS_INVALID"), data);
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<?> handleException(Exception e) {
		log.error("'handleException':", e);
		Map<String, Object> data = new HashMap<>();
		data.put("stackTrace", e.getStackTrace());
		return ResponseEntity.badRequest().body(new ResultVO<Map<String, Object>>(e.getMessage(), data));
	}

}
