package is.rares.kumo.core.exceptions;

import is.rares.kumo.core.exceptions.codes.BaseErrorCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    private String message;
    private int httpStatus = HttpStatus.BAD_REQUEST.value();

    private String errorCode;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(BaseErrorCode errorCode, String message) {
        this.message = message;
        this.httpStatus = errorCode.getHttpStatus().value();
        this.errorCode = errorCode.getErrorCode();
    }

    public ErrorResponse(KumoException exception, String message) {
        this.message = message;
        this.httpStatus = exception.getErrorCode().getHttpStatus().value();
        this.errorCode = exception.getErrorCode().getErrorCode();
    }
}
