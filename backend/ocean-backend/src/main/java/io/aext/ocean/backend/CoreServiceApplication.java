package io.aext.ocean.backend;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
@SpringBootApplication
@ComponentScan(basePackages = { "io.aext.ocean.backend" })
@EnableJpaRepositories(basePackages = { "io.aext.ocean.backend" })
@EntityScan(basePackages = { "io.aext.ocean.backend" })
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
