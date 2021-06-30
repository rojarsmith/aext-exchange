package io.aext.ocean.backend.controller;

import static io.aext.ocean.backend.constant.SystemConstant.CAPTCHA_IP_DELAY_PREFIX;
import static io.aext.ocean.backend.constant.SystemConstant.CAPTCHA_TOKEN_PREFIX;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.aext.ocean.backend.util.CaptchaLite;
import io.aext.ocean.backend.util.IpUtils;

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
	public BufferedImage newImage(HttpServletRequest request) {
		String ip = IpUtils.getIpAddr(request);
		// Read cache
		String key = CAPTCHA_TOKEN_PREFIX + ip;
		String key2 = CAPTCHA_IP_DELAY_PREFIX + ip;
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
	}

	@PostMapping(value = { "/new/base64" })
	@ResponseBody
	public ResponseEntity<?> newBase64(HttpServletRequest request) {
		String ip = IpUtils.getIpAddr(request);
		// Read cache
		String key = CAPTCHA_TOKEN_PREFIX + ip;
		String key2 = CAPTCHA_IP_DELAY_PREFIX + ip;
		String delay = readRedisValueAsString(key2);
		if (!delay.equals("N")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("TRY_IT_LATER"));
		}

		CaptchaLite validateCode = new CaptchaLite();

		List<Object> captcha = validateCode.getRandomCodeBase64();
		String token = (String) captcha.get(0);
		String image = (String) captcha.get(1);

		Map<String, String> data = new HashMap<>();
		data.put("image", "data:image/png;base64," + image);

		updateRedisValueAsString(key2, "Y", 3);
		updateRedisValueAsString(key, token, 600);

		return success(data);
	}

	@Bean
	public HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}
}
