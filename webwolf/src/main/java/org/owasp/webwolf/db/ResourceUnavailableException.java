package org.owasp.webwolf.db;

public class ResourceUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public ResourceUnavailableException() {
        super();
    }

    public ResourceUnavailableException(Throwable cause) {
        super(cause);
    }

    public ResourceUnavailableException(String message) {
        super(message);
    }

    public ResourceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
