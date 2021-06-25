package io.aext.core.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-06-12
 */
@Data
@Component
public class ServiceProperty {
	@Value("${spring.profiles.active}")
	String profilesActive;

	@Value("${service.server.domain}")
	String serverDomain;

	@Value("${service.front.domain}")
	String frontDomain;

	@Value("${service.front.confirm}")
	String frontConfirm;

	@Value("${spring.mail.username}")
	String mailUsername;

	@Value("${service.company}")
	String company;

	@Value("${service.owner.username}")
	String ownerUsername;

	@Value("${service.owner.password}")
	String ownerPassword;

	@Value("${service.owner.email}")
	String ownerEmail;

	public boolean isDev() {
		if (profilesActive.equals("dev") || profilesActive.equals("test")) {
			return true;
		}
		return false;
	}
}
