// edo-service/src/main/java/edo_service/exception/ValidationException.java
package edo_service.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}