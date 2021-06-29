package io.aext.core.service;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author rojar
 *
 * @date 2021-06-28
 */
@SpringBootApplication
@ComponentScan(basePackages = { "io.aext.core.base", "io.aext.core.service" })
@EnableJpaRepositories(basePackages = { "io.aext.core.base" })
@EntityScan(basePackages = { "io.aext.core.base" })
@EnableAsync
public class CoreServiceApplication {
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(CoreServiceApplication.class, args);
	}

}
