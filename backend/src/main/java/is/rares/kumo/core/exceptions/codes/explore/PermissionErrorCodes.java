package is.rares.kumo.core.exceptions.codes.explore;

import is.rares.kumo.core.exceptions.codes.BaseErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum PermissionErrorCodes implements BaseErrorCode {
    DUPLICATE_PERMISSION("1", HttpStatus.CONFLICT, "Permission with this path point and exploration role already exists"),
    ID_MISSING("2", HttpStatus.BAD_REQUEST, "Id is missing"),
    NOT_FOUND("3", HttpStatus.NOT_FOUND, "Path point not found"),

    ;

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String defaultMessage;

    @Override
    public String getErrorCode() {
        return "Permission" + errorCode;
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
