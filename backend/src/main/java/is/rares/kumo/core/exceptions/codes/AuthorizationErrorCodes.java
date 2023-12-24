package is.rares.kumo.core.exceptions.codes;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AuthorizationErrorCodes implements BaseErrorCode {
    UNEXPECTED_ERROR("1", HttpStatus.BAD_REQUEST, "Unexpected error occurred"),
    INVALID_TOKEN("2", HttpStatus.UNAUTHORIZED, "Invalid token"),
    INVALID_REFRESH_TOKEN("3", HttpStatus.UNAUTHORIZED, "Invalid refresh token"),
    UNAUTHORIZED("4", HttpStatus.UNAUTHORIZED, "Access is denied");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String defaultMessage;


    @Override
    public String getErrorCode() {
        return "Authorization" + errorCode;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

