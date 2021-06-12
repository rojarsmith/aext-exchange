package io.aext.core.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author rojar
 *
 * @date 2021-06-12
 */
@SpringBootTest
public class PasswordTest {
	@Test
	public void password() {
		System.out.println(new BCryptPasswordEncoder().encode("abc"));
		System.out.println(new BCryptPasswordEncoder().encode("def"));
		System.out.println(new BCryptPasswordEncoder().encode("ghi"));
	}
}
