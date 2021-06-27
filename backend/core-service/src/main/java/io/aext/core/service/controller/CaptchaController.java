package io.aext.core.service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.aext.core.base.controller.BaseController;
import io.aext.core.base.util.CaptchaLite;
import io.aext.core.service.model.param.NewImage;

/**
 * @author Rojar Smith
 * @Description:
 * @date 2021/06/27
 */
@RestController
@RequestMapping(value = { "/api/v1/captcha" })
public class CaptchaController extends BaseController {
	@PostMapping(value = { "/new/image" })
	@ResponseBody
	public void newImage(HttpServletRequest request, HttpServletResponse response,
			@Validated NewImage param) {
		  response.setContentType("image/png");
          response.setHeader("Cache-Control", "no-cache");
          response.setHeader("Expire", "0");
          response.setHeader("Pragma", "no-cache");
          
          CaptchaLite validateCode = new CaptchaLite();
          
          validateCode.getRandomCodeImage(request, response);
//		// Get member
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		MemberDetails md = (MemberDetails) authentication.getPrincipal();
//
//		Optional<Member> omember = memberService.findByUsername(md.getUsername());
//		if (omember.isEmpty()) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("SYSTEM_ERROR"));
//		}
//		Member member = omember.get();
//
//		// Read cache
//		String key = EMAIL_RESET_PASSWORD_PREFIX + member.getUsername();
//		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//		Object tokenStored = Optional.ofNullable(valueOperations.get(key)).orElse("N");
//		if (!tokenStored.toString().equals("N")) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("EMAIL_ALREADY_SEND"));
//		}
//
//
//		valueOperations.set(key, token, 10, TimeUnit.MINUTES);

//		return success("Check email.");
	}
}
