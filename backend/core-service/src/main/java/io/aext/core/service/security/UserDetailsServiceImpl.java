package io.aext.core.service.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.aext.core.base.entity.Member;
import io.aext.core.base.entity.Permission;
import io.aext.core.base.entity.Role;
import io.aext.core.base.service.LocaleMessageSourceService;
import io.aext.core.base.service.MemberService;
import io.aext.core.base.service.PermissionService;

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

	@Autowired
	PermissionService permissionService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<Member> member = memberService.findByUsername(username);
		if (member.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("MEMBER_NOT_EXISTS"));
		}

		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		for (Role role : member.get().getRoleList()) {
			SimpleGrantedAuthority sga = new SimpleGrantedAuthority(role.getCode());
			authorities.add(sga);

			for (Permission r : role.getPermissionList()) {
				authorities.add(new SimpleGrantedAuthority(r.getId().toString()));
			}
		}

		return new MemberDetails(
				//
				member.get().getId(),
				//
				member.get().getUsername(),
				//
				member.get().getEmail(),
				//
				member.get().getPassword(), true, authorities);
	}

}
