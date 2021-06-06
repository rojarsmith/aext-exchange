package io.aext.core.service.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.aext.core.base.controller.BaseController;
import io.aext.core.base.payload.MessageResponse;
import io.aext.core.base.service.LocaleMessageSourceService;
import io.aext.core.base.service.MemberService;
import io.aext.core.service.payload.RegisterByEmail;

import static org.springframework.util.Assert.isTrue;

/**
 * @author Rojar Smith
 * @Description:
 * @date 2021/6/5
 */
@RestController
public class MemberController extends BaseController {	
	//@Autowired
	//private RedisTemplate redisTemplate;
	
	@Autowired
	LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	MemberService memberService;	

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

			String meesage = localeMessageSourceService.getMessage("RegisterByEmail.register.failed");

			return error(500, meesage, data);
		}

		isTrue(!memberService.isEmailExist(registerByEmail.getEmail()),
				localeMessageSourceService.getMessage("EMAIL_ALREADY_BOUND"));
		isTrue(!memberService.isUsernameExist(registerByEmail.getUsername()),
				localeMessageSourceService.getMessage("USERNAME_ALREADY_EXISTS"));
		
		
		return null;
	}
}