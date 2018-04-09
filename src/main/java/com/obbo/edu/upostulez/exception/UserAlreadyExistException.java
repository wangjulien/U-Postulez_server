package com.obbo.edu.upostulez.exception;

public class UserAlreadyExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5776538376614986550L;

	public UserAlreadyExistException() {
		super();
	}

	public UserAlreadyExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAlreadyExistException(String message) {
		super(message);
	}

	public UserAlreadyExistException(Throwable cause) {
		super(cause);
	}
}
