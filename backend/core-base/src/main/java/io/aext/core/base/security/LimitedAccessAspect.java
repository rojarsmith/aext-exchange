package io.aext.core.base.security;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import io.aext.core.base.service.LocaleMessageSourceService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rojar Smith
 *
 * @date 2021-07-01
 */
@Aspect
@Component
@Slf4j
public class LimitedAccessAspect {
	public static String LIMITED_ACCESS_ASPECT_PREFIX = "LIMITED_ACCESS_ASPECT_";

	@Autowired
	LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	StringRedisTemplate redisTemplate;

	@Pointcut("@annotation(limitedAccess)")
	public void limitAccessPointCut(LimitedAccess limitedAccess) {

	}

	@Around(value = "limitAccessPointCut(limitedAccess)", argNames = "point,limitedAccess")
	public Object doAround(ProceedingJoinPoint point, LimitedAccess limitedAccess) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (null != attributes) {
			String className = point.getTarget().getClass().getName();
			String methodName = point.getSignature().getName();
			HttpServletRequest request = attributes.getRequest();
			String remoteAddr = request.getRemoteAddr();
			if (remoteAddr.contains(":")) {
				remoteAddr = remoteAddr.replace(":", ".");
			}
			log.info("remoteAddr：" + remoteAddr);

			String key = LIMITED_ACCESS_ASPECT_PREFIX + className + "." + methodName + "@" + remoteAddr;
			String keyHeavy = LIMITED_ACCESS_ASPECT_PREFIX + "HEAVY_" + className + "." + methodName + "@" + remoteAddr;

			String value = Optional.ofNullable(redisTemplate.opsForValue().get(key)).orElse("N");
			String valueHeavy = Optional.ofNullable(redisTemplate.opsForValue().get(keyHeavy)).orElse("N");
			Long limit = value == "N" ? 0 : Long.parseLong(value);
			Long heavy = valueHeavy == "N" ? 0 : Long.parseLong(valueHeavy);

			if(heavy >= limitedAccess.heavyFrequency()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
				        localeMessageSourceService.getMessage("TRY_IT_TOMORROW"));
			}
			
			if (limit > 0) {
				if (limit >= limitedAccess.frequency()) {
					if (heavy > 0) {
						heavy++;
						if(heavy >= limitedAccess.heavyFrequency()) {
							redisTemplate.opsForValue().set(keyHeavy, heavy.toString(), limitedAccess.heavyDelay(), TimeUnit.SECONDS);
						}else {
							redisTemplate.opsForValue().set(keyHeavy, heavy.toString());
						}
					} else {
						redisTemplate.opsForValue().set(keyHeavy, "1", limitedAccess.heavySecond(), TimeUnit.SECONDS);
					}
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					        localeMessageSourceService.getMessage("TRY_IT_LATER"));
				}
				limit++;
				redisTemplate.opsForValue().set(key, limit.toString());
			} else {
				redisTemplate.opsForValue().set(key, "1", limitedAccess.second(), TimeUnit.SECONDS);
			}
		}
		return point.proceed();
	}
}

