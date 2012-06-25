package com.onb.srs.exceptions;

public class OverloadException extends Exception {

	public OverloadException() {
	}

	public OverloadException(String message) {
		super(message);
	}

	public OverloadException(Throwable cause) {
		super(cause);
	}

	public OverloadException(String message, Throwable cause) {
		super(message, cause);
	}

}
