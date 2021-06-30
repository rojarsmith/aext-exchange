package io.aext.ocean.backend.enums;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
public enum ResourceType implements BaseEnum {
	MENU, API;

	@Override
	public int getOrdinal() {
		return this.ordinal();
	}
}
