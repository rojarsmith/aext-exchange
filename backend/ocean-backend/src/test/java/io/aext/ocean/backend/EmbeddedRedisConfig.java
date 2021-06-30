package io.aext.ocean.backend;

import javax.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;

import redis.embedded.RedisServer;

/**
 * @author rojar
 *
 * @date 2021-06-25
 */
@TestConfiguration
public class EmbeddedRedisConfig {
	private static RedisServer redisServer = null;

	/**
	 * constructor
	 */
	public EmbeddedRedisConfig(RedisProperties redisProperties) {
		start(redisProperties);
	}

	public static synchronized void start(RedisProperties redisProperties) {
		if (redisServer == null) {
			redisServer = new RedisServer(redisProperties.getPort());
			redisServer.start();
		}
	}

	@PreDestroy
	public void preDestroy() {
		stop();
	}

	public static synchronized void stop() {
		if (redisServer != null) {
			redisServer.stop();
			redisServer = null;
		}
	}
}
