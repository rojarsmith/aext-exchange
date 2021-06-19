package io.aext.core.base.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.aext.core.base.constant.ResourceType;
import io.aext.core.base.model.entity.Permission;

/**
 * @author rojar
 *
 * @date 2021-06-19
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration({ "classpath:spring-service.xml" })
public class PermissionRepositoryTest {
	@Autowired
	PermissionRepository permissionRepository;

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
		permissionRepository.saveAll(Arrays.asList(p1, p2));
	}

	@Test
	public void commonTest() {
		Optional<Permission> q1 = permissionRepository.findById(1001L);
		assertEquals(true, q1.isPresent());
		List<Permission> q2 = permissionRepository.findAllById(Arrays.asList(1001L, 1002L));
		assertEquals(2, q2.size());
		List<Permission> q3 = permissionRepository.findById(1001L, 1001L);
		assertEquals(1, q3.size());
		List<Permission> q4 = permissionRepository.findByIdGreaterThanEqualAndIdLessThanEqual(1001L, 1002L);
		assertEquals(2, q4.size());
	}
}
