package ch.interlis.iox_j;

import ch.interlis.iox.IoxException;

public class IoxSyntaxException extends IoxException {

	public IoxSyntaxException() {
	}

	public IoxSyntaxException(String message) {
		super(message);
	}

	public IoxSyntaxException(Throwable cause) {
		super(cause);
	}

	public IoxSyntaxException(int lineNumber, String message) {
		super(lineNumber, message);
	}

	public IoxSyntaxException(int lineNumber, Throwable cause) {
		super(lineNumber, cause);
	}

	public IoxSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public IoxSyntaxException(int lineNumber, String message, Throwable cause) {
		super(lineNumber, message, cause);
	}

}
