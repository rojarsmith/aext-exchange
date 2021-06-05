package io.aext.core.service.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.aext.core.base.controller.BaseController;
import io.aext.core.base.payload.MessageResponse;
import io.aext.core.base.util.JsonHelper;
import io.aext.core.service.payload.RegisterByEmail;

/**
 * @author Rojar Smith
 * @Description:
 * @date 2021/6/5
 */
@RestController
public class Member extends BaseController {
	@Autowired
	MessageSource messageSource;

	/**
	 * Register by email.
	 */
	@RequestMapping("/v1/member/register/email")
	@ResponseBody
	public MessageResponse registerByEmail(@Valid RegisterByEmail registerByEmail, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			Map<String, List<Map<String, String>>> data = new HashMap<>();
			List<Map<String, String>> errors = new ArrayList<>();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				Map<String, String> error = new HashMap<>();
				error.put("field", fieldError.getField());
				error.put("message", fieldError.getDefaultMessage());
				errors.add(error);
			}
			data.put("errors", errors);

			String deb = messageSource.getMessage("RegisterByEmail.email.format", null, new Locale("zh_CN"));

			return error(500, "Register failed", data);
		}

		return null;
	}
}
