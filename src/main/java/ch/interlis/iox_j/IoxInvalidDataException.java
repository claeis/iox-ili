package ch.interlis.iox_j;

import ch.interlis.iox.IoxException;

public class IoxInvalidDataException extends IoxException {

	public IoxInvalidDataException() {
	}

	public IoxInvalidDataException(String message) {
		super(message);
	}

	public IoxInvalidDataException(Throwable cause) {
		super(cause);
	}

	public IoxInvalidDataException(int lineNumber, String message) {
		super(lineNumber, message);
	}

	public IoxInvalidDataException(int lineNumber, Throwable cause) {
		super(lineNumber, cause);
	}

	public IoxInvalidDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public IoxInvalidDataException(int lineNumber, String message,
			Throwable cause) {
		super(lineNumber, message, cause);
	}

}
