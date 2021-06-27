package io.aext.core.service.controller;

import static io.aext.core.base.constant.SystemConstant.CAPTCHA_PREFIX;
import static io.aext.core.base.constant.SystemConstant.IP_DELAY_PREFIX;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.aext.core.base.controller.BaseController;
import io.aext.core.base.util.CaptchaLite;
import io.aext.core.base.util.IpUtils;
import io.aext.core.service.model.param.NewImage;

/**
 * @author Rojar Smith
 * @Description:
 * @date 2021/06/27
 */
@RestController
@RequestMapping(value = { "/api/v1/captcha" })
public class CaptchaController extends BaseController {
	@Autowired
	StringRedisTemplate redisTemplate;

	@PostMapping(value = { "/new/png" }, produces = "image/png")
	@ResponseBody
	public BufferedImage newImage(HttpServletRequest request, HttpServletResponse response, @Validated NewImage param) {
		String ip = IpUtils.getIpAddr(request);
		// Read cache
		String key = CAPTCHA_PREFIX + ip;
		String key2 = IP_DELAY_PREFIX + ip;
		String delay = readRedisValueAsString(key2);
		if (!delay.equals("N")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("TRY_IT_LATER"));
		}

		CaptchaLite validateCode = new CaptchaLite();

		List<Object> captcha = validateCode.getRandomCodeImage();

		String token = (String) captcha.get(0);
		BufferedImage image = (BufferedImage) captcha.get(1);

		updateRedisValueAsString(key2, "Y", 3);
		updateRedisValueAsString(key, token, 600);

		return image;

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

	@Bean
	public HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}
}
