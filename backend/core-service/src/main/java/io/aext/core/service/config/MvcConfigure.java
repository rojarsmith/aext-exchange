package io.aext.core.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author rojar
 *
 * @date 2021-06-05
 */
@Configuration
public class MvcConfigure implements WebMvcConfigurer {
	@Bean
	public ResourceBundleMessageSource getMessageSource() {
		ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
		resourceBundleMessageSource.setDefaultEncoding("UTF-8");
		resourceBundleMessageSource.setBasenames("lang/validations");
		resourceBundleMessageSource.setCacheSeconds(3600);
		return resourceBundleMessageSource;
	}

	@Override
	public Validator getValidator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(getMessageSource());
		return validator;
	}
}
