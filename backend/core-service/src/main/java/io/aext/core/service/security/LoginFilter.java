package io.aext.core.service.security;

import static io.aext.core.base.constant.SystemConstant.JWT_LOGOUT_PREFIX;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.aext.core.base.util.SHA2;
import io.jsonwebtoken.Claims;

/**
 * @author rojar
 *
 * @date 2021-07-02
 */
@Component
public class LoginFilter extends OncePerRequestFilter {
	@Autowired
	private JwtManager jwtManager;

	@Autowired
	private UserDetailsServiceImpl userService;

	@Autowired
	StringRedisTemplate redisTemplate;

	@SuppressWarnings("rawtypes")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		Claims claims = jwtManager.parse(request.getHeader("Authorization"));
		if (claims != null) {
			String username = claims.getSubject();
			MemberDetails user = (MemberDetails) userService.loadUserByUsername(username);
			// Check token is logout, if token logout return anonymousUser.
			String tokenHash = SHA2.getSHA256Short(request.getHeader("Authorization"), 0, 8).toUpperCase();
			ValueOperations valueOperations = redisTemplate.opsForValue();
			Object cache = Optional
					.ofNullable(valueOperations.get(JWT_LOGOUT_PREFIX + user.getUsername() + "_" + tokenHash))
					.orElse("N");
			if (cache.toString().equals("N")) {
				user.setJwtHash(tokenHash);
				Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
						user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}
}
