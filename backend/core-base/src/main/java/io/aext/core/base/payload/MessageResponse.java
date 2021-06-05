package io.aext.core.base.payload;

import io.aext.core.base.util.JsonHelper;
import lombok.Data;

@Data
public class MessageResponse {
	private int code;
	private String message;
	private Object data;
	private int totalPage;
	private int totalElement;

	public MessageResponse(String msg) {
		this(0, msg);
	}

	public MessageResponse(int code, String msg) {
		// Must set null to avoid error:
		// No serializer found for class java.lang.Object
		this(code, msg, null);
	}

	public MessageResponse(int code, String msg, Object object) {
		this.code = code;
		this.message = msg;
		this.data = object;
	}

	@Override
	public String toString() {
		return JsonHelper.stringify(this);
	}

	public static MessageResponse success() {
		return new MessageResponse(0, "SUCCESS");
	}

	public static MessageResponse success(String msg) {
		return new MessageResponse(0, msg);
	}

	public static MessageResponse success(String msg, Object data) {
		return new MessageResponse(0, msg, data);
	}

	public static MessageResponse error(int code, String msg) {
		return new MessageResponse(code, msg);
	}

	public static MessageResponse error(String msg) {
		return new MessageResponse(500, msg);
	}
}
