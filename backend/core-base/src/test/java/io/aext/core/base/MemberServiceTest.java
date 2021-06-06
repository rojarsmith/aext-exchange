package io.aext.core.base;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.aext.core.base.service.BaseService;
import io.aext.core.base.service.MemberService;

/**
 * @author rojar
 *
 * @date 2021-06-05
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration({"classpath:spring-service.xml"})
public class MemberServiceTest {
	@Autowired
	MemberService memberService;
	
	@Test
	public void commonTest() {
		BaseService<?> bs = new MemberService();
		
		boolean isEmailExist = memberService.isEmailExist("");
		assertEquals(isEmailExist, false);
		MemberService ms1 = new MemberService();
		assertEquals(ms1.isEmailExist(""), false);
	}
}
