package io.aext.core.service.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

/**
 * @author rojar
 *
 * @date 2021-06-15
 */
@Component
public class LoginFilter extends OncePerRequestFilter {
	@Autowired
	private JwtManager jwtManager;
	@Autowired
	private UserDetailsServiceImpl userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		Claims claims = jwtManager.parse(request.getHeader("Authorization"));
		if (claims != null) {
			String username = claims.getSubject();
			UserDetails user = userService.loadUserByUsername(username);
			Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
					user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}
}
