package io.aext.core.base;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.aext.core.base.model.vo.ECVerifyCodeVO;
import io.aext.core.base.service.EmailContentBuilder;

/**
 * @author rojar
 *
 * @date 2021-06-07
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration({ "classpath:spring-service.xml" })
public class MailContentBuilderTest {
	@Autowired
	EmailContentBuilder mailContentBuilder;

	@Test
	public void commonTest() {
		ECVerifyCodeVO mcVerifyCode = new ECVerifyCodeVO();
		mcVerifyCode.setSubject("TESTかいはつ");
		mcVerifyCode.setCode("123456");
		Optional<String> context1 = mailContentBuilder.generateMailContent(mcVerifyCode);
		assertTrue(context1.isPresent());
	}
}
