package edo_public_api.exception;


public class AdditionalApprovalValidationException extends RuntimeException {

    public AdditionalApprovalValidationException(String message) {
        super(message);
    }

    public AdditionalApprovalValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}