package is.rares.kumo.core.exceptions.codes;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AccountCodeErrorCodes  implements BaseErrorCode{
    UNEXPECTED_ERROR("1", HttpStatus.BAD_REQUEST, "Exception.Unexpected"),
    USERNAME_NOT_FOUND("2", HttpStatus.NOT_FOUND, "Exception.Username.NotFound"),
    PASSWORD_INCORRECT("3", HttpStatus.BAD_REQUEST, "Exception.Password.Incorrect");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String messageKey;


    @Override
    public String getErrorCode() {
        return "AcccountCode" + errorCode;
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
