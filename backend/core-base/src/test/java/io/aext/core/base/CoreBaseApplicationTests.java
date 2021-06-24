package io.aext.core.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.aext.core.base.enums.ResourceType;
import io.aext.core.base.model.entity.Permission;
import io.aext.core.base.repository.PermissionRepository;

/**
 * @author rojar
 *
 * @date 2021-06-24
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration({ "classpath:spring-service.xml" })
public class CoreBaseApplicationTests {
	@Autowired
	PermissionRepository permissionRepository;

	@PostConstruct
//	@Sql(scripts = { "/import-test.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
		permissionRepository.saveAll(ps1);
	}
}
