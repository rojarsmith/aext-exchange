package io.aext.core.base.enums;

/**
 * @author rojar
 *
 * @date 2021-06-15
 */
public enum ResourceType implements BaseEnum {
	MENU, API;

	@Override
	public int getOrdinal() {
		return this.ordinal();
	}
}
