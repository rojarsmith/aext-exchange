package io.aext.core.base.config;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * @author rojar
 *
 * @date 2021-06-05
 */
@Configuration
public class QuerydslConfig {
	@Bean
	public JPAQueryFactory getJPAQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}
}
