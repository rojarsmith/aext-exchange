package io.aext.core.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author rojar
 *
 * @date 2021-06-12
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${server.servlet.context-path}")
	String contextPath;

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.formLogin().disable().authorizeRequests().antMatchers(contextPath + "/h2-console/**").permitAll()
//				.antMatchers("/h2-console/**").permitAll();
//
//	}

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers(contextPath + "/h2-console/**").antMatchers("/h2-console/**");
//	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/h2-console/**");
	}
}
