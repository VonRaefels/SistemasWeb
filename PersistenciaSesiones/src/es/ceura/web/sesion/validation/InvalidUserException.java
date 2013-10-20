package es.ceura.web.sesion.validation;

public class InvalidUserException extends Exception {

	private static final long serialVersionUID = 5233733442004664246L;

	public InvalidUserException() {		
	}

	public InvalidUserException(String message) {
		super(message);		
	}

	public InvalidUserException(Throwable cause) {
		super(cause);		
	}

	public InvalidUserException(String message, Throwable cause) {
		super(message, cause);		
	}

	public InvalidUserException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);		
	}
}
