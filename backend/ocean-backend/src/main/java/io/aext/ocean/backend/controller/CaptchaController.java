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
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.aext.ocean.backend.util.CaptchaLite;
import io.aext.ocean.backend.util.IpUtils;

/**
 * @author Rojar Smith
 * @Description:
 * @date 2021/07/01
 */
@RestController
@RequestMapping(value = { "/api/v1/captcha" })
public class CaptchaController extends BaseController {
	@Autowired
	StringRedisTemplate redisTemplate;

	@PostMapping(value = { "/new/png" }, produces = "image/png")
	@ResponseBody
	public BufferedImage newImage(HttpServletRequest request) {
		// Check cache
		String ip = IpUtils.getIpAddr(request);
		String keyToken = CAPTCHA_TOKEN_PREFIX + ip;
		String keyDelay = CAPTCHA_IP_DELAY_PREFIX + ip;
		String delay = readRedisValueAsString(keyDelay);
		if (!equalsN(delay)) {
			throwResponseStatusException("TRY_IT_LATER");
		}

		// Generate captcha
		CaptchaLite validateCode = new CaptchaLite();
		List<Object> captcha = validateCode.getRandomCodeImage();
		String token = (String) captcha.get(0);
		BufferedImage image = (BufferedImage) captcha.get(1);

		// Update cache
		updateRedisValueAsString(keyDelay, "Y", 3);
		updateRedisValueAsString(keyToken, token, 600);

		return image;
	}

	@PostMapping(value = { "/new/base64" })
	@ResponseBody
	public ResponseEntity<?> newBase64(HttpServletRequest request) {
		// Check cache
		String ip = IpUtils.getIpAddr(request);
		String keyToken = CAPTCHA_TOKEN_PREFIX + ip;
		String keyDelay = CAPTCHA_IP_DELAY_PREFIX + ip;
		String delay = readRedisValueAsString(keyDelay);
		if (!equalsN(delay)) {
			throwResponseStatusException("TRY_IT_LATER");
		}

		// Generate captcha
		CaptchaLite validateCode = new CaptchaLite();
		List<Object> captcha = validateCode.getRandomCodeBase64();
		String token = (String) captcha.get(0);
		String image = (String) captcha.get(1);
		Map<String, String> data = new HashMap<>();
		data.put("image", "data:image/png;base64," + image);

		// Update cache
		updateRedisValueAsString(keyDelay, "Y", 3);
		updateRedisValueAsString(keyToken, token, 600);

		return success(data);
	}

	@Bean
	public HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}
}