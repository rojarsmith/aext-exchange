package io.aext.core.base.util;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-06-06
 */
@Data
@Component
public class Foo {
	private String name1;

	public Foo() {
		this.name1 = "rojar";
	}

	public String format() {
		return "foo";
	}
}
