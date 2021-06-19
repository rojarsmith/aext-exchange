package io.aext.core.service.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import io.aext.core.base.model.entity.Permission;
import lombok.Getter;

/**
 * @author rojar
 *
 * @date 2021-06-16
 */
@Component
public class SecurityMetadataSourceImpl implements SecurityMetadataSource {
	/**
	 * All of the url of source
	 */
	@Getter
	private static final Set<Permission> RESOURCES = new HashSet<>();

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		FilterInvocation filterInvocation = (FilterInvocation) object;
		HttpServletRequest request = filterInvocation.getRequest();
		for (Permission resource : RESOURCES) {
			// url： GET:/API/user/test/{id}
			String[] split = resource.getPath().split(":");
			AntPathRequestMatcher ant = new AntPathRequestMatcher(split[1]);
			if (request.getMethod().equals(split[0]) && ant.matches(request)) {
				return Collections.singletonList(new SecurityConfig(resource.getId().toString()));
			}
		}
		// No need auth.
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
