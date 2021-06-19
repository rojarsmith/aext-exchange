package io.aext.core.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.aext.core.base.model.vo.ResultVO;

public class MessageResponseTest {
	@Test
	public void commonTest() {
		ResultVO<Object> mr1 = new ResultVO<Object>("OK");
		String json1 = mr1.toString();
		assertEquals("{\"message\":\"OK\",\"data\":null}", json1);
	}
}
