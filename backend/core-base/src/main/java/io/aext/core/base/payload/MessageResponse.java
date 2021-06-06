package io.aext.core.base.payload;

import io.aext.core.base.util.JsonHelper;
import lombok.Data;

@Data
public class MessageResponse {
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
		this(msg, null);
	}

	public MessageResponse(String msg, Object object) {
		this.message = msg;
		this.data = object;
	}

	@Override
	public String toString() {
		return JsonHelper.stringify(this);
	}
}
