package io.aext.core.service.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.server.ResponseStatusException;

import io.aext.core.base.service.LocaleMessageSourceService;

/**
 * @author rojar
 *
 * @date 2021-06-17
 */
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
	@Autowired
	LocaleMessageSourceService localeMessageSourceService;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
				localeMessageSourceService.getMessage("SYSTEM_ERROR"));


	}

}
