package is.rares.kumo.core.exceptions.codes;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AccountCodeErrorCodes implements BaseErrorCode {
    UNEXPECTED_ERROR("1", HttpStatus.BAD_REQUEST, "Unexpected error occurred"),
    USERNAME_NOT_FOUND("2", HttpStatus.NOT_FOUND, "Username or email not found"),
    PASSWORD_INCORRECT("3", HttpStatus.UNAUTHORIZED, "Password incorrect"),

    INVALID_INVITE("4", HttpStatus.UNAUTHORIZED, "Invalid invitation"),

    DUPLICATE_USERNAME("5", HttpStatus.BAD_REQUEST, "Username already used"),
    DUPLICATE_EMAIL("6", HttpStatus.BAD_REQUEST, "Email already use"),

    TWO_FACTOR_MISSING("7", HttpStatus.BAD_REQUEST, "Missing 2FA"),

    ACCOUNT_CODE_NOT_FOUND("8", HttpStatus.NOT_FOUND, "Invalid 2FA Code"),

    ACCOUNT_CODE_EXPIRED("9", HttpStatus.UNPROCESSABLE_ENTITY, "2FA Code Expired"),

    ACCOUNT_CODE_USED("10", HttpStatus.UNPROCESSABLE_ENTITY, "2FA Code already used");


    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String defaultMessage;


    @Override
    public String getErrorCode() {
        return "AccountCode" + errorCode;
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
