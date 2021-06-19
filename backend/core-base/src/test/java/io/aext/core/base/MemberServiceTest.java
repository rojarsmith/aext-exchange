package io.aext.core.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import io.aext.core.base.model.entity.Member;
import io.aext.core.base.service.MemberService;

/**
 * @author rojar
 *
 * @date 2021-06-05
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration({ "classpath:spring-service.xml" })
public class MemberServiceTest {
	@Autowired
	MemberService memberService;

//	@PostConstruct
//	void init() {
//		Member m1 = new Member();
//		m1.setEmail("rojarsmith@gmail.com");
//		m1.setUsername("Rojar");
//		m1.setPassword("abc");
//		memberService.save(m1);
//
//		Member m2 = new Member();
//		m2.setEmail("dev@aext.io");
//		m2.setUsername("Dev かいはつ");
//		m2.setPassword("abc");
//		memberService.save(m2);
//	}

	@Test
	@Transactional
	public void commonTest() {
		boolean isEmailExist = memberService.isEmailExist("");
		assertEquals(isEmailExist, false);
		boolean isEmailExist2 = memberService.isEmailExist("dev@aext.io");
		assertEquals(isEmailExist2, true);

		Executable executable = new Executable() {
			public void execute() {
				MemberService ms1 = new MemberService();
				ms1.isEmailExist("");
			}
		};
		assertThrows(Exception.class, executable);

		MemberService ms1 = new MemberService();
		assertThrows(Exception.class, () -> {
			ms1.isEmailExist("");
		});

		Optional<Member> m3 = memberService.findByEmail("dev@aext.io");
		assertEquals(m3.isPresent(), true);
	}
}
