package io.aext.core.service.security;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * @author rojar
 *
 * @date 2021-06-16
 */
@Component
public class DecisionManagerImpl implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		if (configAttributes == null || configAttributes.isEmpty()) {
			return;
		}

		for (ConfigAttribute ca : configAttributes) {
			for (GrantedAuthority authority : authentication.getAuthorities()) {
				if (Objects.equals(authority.getAuthority(), ca.getAttribute())) {
					return;
				}
			}
		}
		throw new AccessDeniedException("No auth");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
