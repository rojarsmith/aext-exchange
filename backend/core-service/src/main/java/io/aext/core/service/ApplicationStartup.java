package io.aext.core.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import io.aext.core.base.enums.MemberStatus;
import io.aext.core.base.enums.ResourceType;
import io.aext.core.base.model.entity.Member;
import io.aext.core.base.model.entity.Permission;
import io.aext.core.base.model.entity.Role;
import io.aext.core.base.service.MemberService;
import io.aext.core.base.service.PermissionService;
import io.aext.core.base.service.RoleService;
import io.aext.core.service.security.Auth;
import io.aext.core.service.security.SecurityMetadataSourceImpl;

/**
 * @author rojar
 *
 * @date 2021-06-25
 */
@Component
public class ApplicationStartup implements ApplicationRunner {
	@Autowired
	ServiceProperty serviceProperty;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private RequestMappingInfoHandlerMapping requestMappingInfoHandlerMapping;
	@Autowired
	MemberService memberService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService resourceService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// Scan all auth and write to database.
		List<Permission> list = getAuthResources();

		resourceService.delete(ResourceType.API);

		if (list != null && !list.isEmpty()) {
			SecurityMetadataSourceImpl.getRESOURCES().addAll(list);
			resourceService.update(list);
		}

		initDatabase();
	}

	/*
	 * Scan and get all resources auth.
	 */
	private List<Permission> getAuthResources() {
		List<Permission> list = new LinkedList<>();
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingInfoHandlerMapping.getHandlerMethods();
		for (Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			System.out.println("key:" + entry.getKey() + ",value:" + entry.getValue());
			Auth moduleAuth = entry.getValue().getBeanType().getAnnotation(Auth.class);
			Auth methodAuth = entry.getValue().getMethod().getAnnotation(Auth.class);
			if (moduleAuth == null || methodAuth == null) {
				continue;
			}

			Set<RequestMethod> methods = entry.getKey().getMethodsCondition().getMethods();

			if (methods.size() != 1) {
				continue;
			}

			// Combine the auth path with ':', GET:/user/{id}、POST:/user/{id}
			String path = methods.toArray()[0] + ":" + entry.getKey().getPatternsCondition().getPatterns().toArray()[0];
			Permission resource = new Permission();
			resource.setType(ResourceType.API)
					//
					.setPath(path)
					//
					.setName(methodAuth.name())
					//
					.setId(moduleAuth.id() + methodAuth.id());
			list.add(resource);
		}
		return list;
	}

	void initDatabase() {
		if (serviceProperty.isDev()) {

			// Create Roles

			List<Permission> permissionListAdmin = resourceService.readPermissions(ResourceType.API);
			Role roleAdmin = new Role("ROLE_ADMIN", "Admin", permissionListAdmin);
			roleService.update(roleAdmin);

			List<Permission> permissionListMember = new ArrayList<>(permissionListAdmin).stream()
					//
					.filter(x -> x.getId() == 1001)
					//
					.collect(Collectors.toList());
			permissionListMember.remove(0);
			Role roleMember = new Role("ROLE_MEMBER", "Member", permissionListMember);
			roleService.update(Arrays.asList(roleAdmin, roleMember));

			// Creating user's account

			List<Member> members = new ArrayList<>();
			String utcTimeOwner = "2021-12-21T12:26:11Z";
			Instant instantOwner = Instant.parse(utcTimeOwner);
			Member memberOwner = new Member();
			memberOwner.setUsername(serviceProperty.getOwnerUsername());
			memberOwner.setPassword(passwordEncoder.encode(serviceProperty.getOwnerPassword()));
			memberOwner.setEmail(serviceProperty.getOwnerEmail());
			memberOwner.setRegistTime(instantOwner);
			memberOwner.setMemberLevel(EnumSet.of(MemberStatus.REGISTERD, MemberStatus.VERIFIED_EMAIL));
			memberOwner.setRoleList(Arrays.asList(roleAdmin));
			members.add(memberOwner);

			Member member = new Member();
			member.setUsername("aaa");
			member.setPassword(passwordEncoder.encode("11112222"));
			member.setEmail("aaa@aext.io");
			member.setRegistTime(Instant.now());
			member.setMemberLevel(EnumSet.of(MemberStatus.REGISTERD));
			member.setRoleList(Arrays.asList(roleMember));
			members.add(member);

			for (int i = 1; i <= 100; i++) {
				Member memberGen1 = new Member();
				memberGen1.setUsername("user" + i);
				memberGen1.setPassword(passwordEncoder.encode("11112222"));
				memberGen1.setEmail("email" + i + "@aext.io");
				if (i <= 50) {
					memberGen1.setRegistTime(Instant.now());
					memberGen1.setMemberLevel(EnumSet.of(MemberStatus.REGISTERD));
				} else {
					memberGen1.setRegistTime(Instant.now().plusSeconds(43200));
					memberGen1.setMemberLevel(EnumSet.of(MemberStatus.REGISTERD, MemberStatus.ILLEGAL));
				}
				memberGen1.setRoleList(Arrays.asList(roleMember));
				members.add(memberGen1);
			}

			memberService.update(members);
		}
	}
}
