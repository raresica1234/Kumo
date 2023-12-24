package is.rares.kumo.core.exceptions.codes;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AuthorizationErrorCodes implements BaseErrorCode {
    UNEXPECTED_ERROR("1", HttpStatus.BAD_REQUEST, "Exception.Unexpected"),
    INVALID_TOKEN("2", HttpStatus.UNAUTHORIZED, "Exception.Token.Invalid"),
    INVALID_REFRESH_TOKEN("3", HttpStatus.UNAUTHORIZED, "Exception.RefreshToken.Invalid"),
    UNAUTHORIZED("4", HttpStatus.UNAUTHORIZED, "Exception.Token.Unauthorized");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String messageKey;


    @Override
    public String getErrorCode() {
        return "Authorization" + errorCode;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

