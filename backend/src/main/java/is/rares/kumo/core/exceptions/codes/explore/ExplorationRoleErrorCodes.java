package is.rares.kumo.core.exceptions.codes.explore;

import is.rares.kumo.core.exceptions.codes.BaseErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ExplorationRoleErrorCodes implements BaseErrorCode {
    DUPLICATE_ROLE("1", HttpStatus.CONFLICT, "Exploration role already exists"),
    ID_MISSING("2", HttpStatus.BAD_REQUEST, "Id is missing"),
    NOT_FOUND("3", HttpStatus.NOT_FOUND, "Exploration role not found")
    ;

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String defaultMessage;

    @Override
    public String getErrorCode() {
        return "ExplorationRole" + errorCode;
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
