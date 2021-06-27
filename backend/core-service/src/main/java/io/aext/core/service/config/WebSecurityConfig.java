package io.aext.core.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.aext.core.service.security.AccessDeniedHandlerImpl;
import io.aext.core.service.security.AuthFilter;
import io.aext.core.service.security.LoginFilter;
import io.aext.core.service.security.UserDetailsServiceImpl;

/**
 * @author rojar
 *
 * @date 2021-06-25
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private LoginFilter loginFilter;

	@Autowired
	private AuthFilter authFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().formLogin().disable();
		http.authorizeRequests()
				//
				.antMatchers("/api/v1/captcha/new/png").permitAll()
				//
				.antMatchers("/api/v1/captcha/new/base64").permitAll()
				//
				.antMatchers("/api/v1/member/register").permitAll()
				//
				.antMatchers("/api/v1/member/activate").permitAll()
				//
				.antMatchers("/api/v1/member/password/forget").permitAll()
				//
				.antMatchers("/api/v1/member/verify").permitAll()
				//
				.antMatchers("/api/v1/member/login").permitAll()
				//
				.antMatchers("/api/v1/member/logout").permitAll()
				//
				.antMatchers("/api/v1/member/test").permitAll()
				//
				.antMatchers("/api/v1/member/test2").permitAll()
				//
				.anyRequest().authenticated()
				//
				.and().exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl());
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Authentication filters.
		http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(authFilter, FilterSecurityInterceptor.class);
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	/**
	 * Encrypt password.
	 *
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
