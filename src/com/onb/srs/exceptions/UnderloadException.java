package com.onb.srs.exceptions;

public class UnderloadException extends Exception {

	public UnderloadException() {
	}

	public UnderloadException(String message) {
		super(message);
	}

	public UnderloadException(Throwable cause) {
		super(cause);
	}

	public UnderloadException(String message, Throwable cause) {
		super(message, cause);
	}

}
