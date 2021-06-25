package io.aext.core.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import io.aext.core.base.enums.ResourceType;
import io.aext.core.base.model.entity.Member;
import io.aext.core.base.model.entity.Permission;
import io.aext.core.base.model.entity.Role;
import io.aext.core.base.service.MemberService;
import io.aext.core.base.service.PermissionService;
import io.aext.core.base.service.RoleService;

/**
 * @author rojar
 *
 * @date 2021-06-19
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration({ "classpath:spring-service.xml" })
public class PermissionServiceTest {
	@Autowired
	MemberService memberService;

	@Autowired
	RoleService roleService;

	@Autowired
	PermissionService permissionService;

	@PostConstruct
	void init() {
		Permission p1 = new Permission();
		p1.setId(1001L)
				//
				.setName("create new user")
				//
				.setPath("PUT:/api/v1/member/test")
				//
				.setType(ResourceType.API);

		Permission p2 = new Permission();
		p2.setId(1002L)
				//
				.setName("Delete user")
				//
				.setPath("DELETE:/api/v1/member/test2")
				//
				.setType(ResourceType.API);
		permissionService.update(Arrays.asList(p1, p2));

		List<Permission> ps1 = new ArrayList<>();
		for (int i = 1; i <= 99; i++) {
			Permission p = new Permission();
			p.setId(2000L + i)
					//
					.setName("Name " + i)
					//
					.setPath("DELETE:/api/v1/member/test" + i)
					//
					.setType(ResourceType.API);

			ps1.add(p);
		}
		permissionService.update(ps1);

		Role role = new Role("ROLE_ADMIN", "Admin", Arrays.asList(p1, p2));
		roleService.update(role);

		Member m1 = new Member();
		m1.setEmail("rojarsmith@gmail.com");
		m1.setUsername("Rojar");
		m1.setPassword("abc");
		m1.setRoleList(Arrays.asList(role));
		memberService.save(m1);

		Member m2 = new Member();
		m2.setEmail("dev@aext.io");
		m2.setUsername("Dev かいはつ");
		m2.setPassword("abc");
		m2.setRoleList(Arrays.asList(role));
		memberService.save(m2);
	}

	@Test
	@Transactional
	public void commonTest() {
		Optional<Member> member = memberService.findByUsername("Rojar");
		List<Permission> permissions = permissionService.readPermissions(member.get().getId());
		assertEquals(2, permissions.size());

		List<Permission> permissions2 = permissionService.readPermissions(ResourceType.API, PageRequest.of(1, 5));
		assertEquals(5, permissions2.size());
	}
}
