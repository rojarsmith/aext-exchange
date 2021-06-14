package io.aext.core.service.security;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.aext.core.base.entity.Member;
import io.aext.core.base.service.LocaleMessageSourceService;
import io.aext.core.base.service.MemberService;

/**
 * @author rojar
 *
 * @date 2021-06-14
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	MemberService memberService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<Member> member = memberService.findByUsername(username);
		if (member.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("MEMBER_NOT_EXISTS"));
		}
		return new MemberDetails(
				//
				member.get().getId(),
				//
				member.get().getUsername(),
				//
				member.get().getEmail(),
				//
				member.get().getPassword(), true, Collections.emptyList());
	}

}
