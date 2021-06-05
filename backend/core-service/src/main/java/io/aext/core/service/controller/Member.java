package io.aext.core.service.controller;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.aext.core.base.controller.BaseController;
import io.aext.core.base.payload.MessageResponse;
import io.aext.core.service.payload.RegisterByEmail;

/**
 * @author Rojar Smith
 * @Description:
 * @date 2021/6/5
 */
@RestController
@RequestMapping("member")
public class Member extends BaseController {
	/**
	 * 邮箱注册
	 *
	 * @param loginByEmail
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping("/register/email")
	@ResponseBody
	public MessageResponse registerByEmail(@Valid RegisterByEmail registerByEmail, BindingResult bindingResult)
			throws Exception {
		return null;
	}
}
