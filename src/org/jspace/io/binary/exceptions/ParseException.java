package org.jspace.io.binary.exceptions;

import java.io.IOException;

public class ParseException extends IOException {
	private static final long serialVersionUID = 1L;

	public ParseException() {}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(Throwable cause) {
		super(cause);
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
