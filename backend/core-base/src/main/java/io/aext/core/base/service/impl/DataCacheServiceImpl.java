package io.aext.core.base.service.impl;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import io.aext.core.base.service.DataCacheService;

/**
 * @author Rojar Smith
 *
 * @date 2021-07-02
 */
@Service
public class DataCacheServiceImpl implements DataCacheService {
	@Autowired
	StringRedisTemplate redisTemplate;

	public Optional<String> readString(String key) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		return Optional.ofNullable(valueOperations.get(key));
	}

	public void update(String key, String value, int seconds) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value, seconds, TimeUnit.SECONDS);
	}

	public void delete(String key) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.getOperations().delete(key);
	}
}
