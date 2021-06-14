package io.aext.core.service;

import java.time.Instant;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.aext.core.base.constant.MemberStatus;
import io.aext.core.base.entity.Member;
import io.aext.core.base.service.MemberService;

/**
 * @author rojar
 *
 * @date 2021-06-14
 */
@Component
public class ApplicationStartup implements ApplicationRunner {
	@Autowired
	ServiceProperty serviceProperty;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	MemberService memberService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (serviceProperty.isDev()) {
			// Creating user's account
			Member member = new Member();
			member.setUsername("service");
			member.setPassword(passwordEncoder.encode("11112222"));
			member.setEmail("service@aext.io");
			member.setRegistTime(Instant.now());
			member.setMemberLevel(EnumSet.of(MemberStatus.REGISTERD));
			memberService.save(member);
		}

	}

}
