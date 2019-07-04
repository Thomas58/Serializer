package org.jspace.io.xml.exceptions;

public class XMLSyntaxException extends Exception {
	private static final long serialVersionUID = 1L;

	public XMLSyntaxException() {
		super();
	}

	public XMLSyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public XMLSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public XMLSyntaxException(String message) {
		super(message);
	}

	public XMLSyntaxException(Throwable cause) {
		super(cause);
	}
}
