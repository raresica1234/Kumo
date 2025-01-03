package is.rares.kumo.core.exceptions.codes.explore;

import is.rares.kumo.core.exceptions.codes.BaseErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ExplorationUserRoleErrorCodes implements BaseErrorCode {
    USER_NOT_FOUND("1", HttpStatus.NOT_FOUND, "User not found"),
    EXPLORATION_ROLE_NOT_FOUND("2", HttpStatus.NOT_FOUND, "Exploration role not found"),
    USER_EXPLORATION_ROLE_NOT_FOUND("3", HttpStatus.NOT_FOUND, "User does not have that exploration role"),

    ;

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String defaultMessage;

    @Override
    public String getErrorCode() {
        return "ExplorationUserRole" + errorCode;
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
