package io.aext.core.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-08
 */
@Configuration
public class ThymeleafConfig {
	@Bean
	public ClassLoaderTemplateResolver thymeleafTemplateResolver() {
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("/templates/");
		resolver.setSuffix(".html");
		resolver.setCacheable(true);
		resolver.setCharacterEncoding("UTF-8");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setOrder(0);
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(thymeleafTemplateResolver());
		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}
}
