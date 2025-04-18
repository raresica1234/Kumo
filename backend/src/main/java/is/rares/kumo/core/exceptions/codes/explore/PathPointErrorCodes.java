package is.rares.kumo.core.exceptions.codes.explore;

import org.springframework.http.HttpStatus;

import is.rares.kumo.core.exceptions.codes.BaseErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PathPointErrorCodes implements BaseErrorCode {
    DUPLICATE_PATH_POINT("1", HttpStatus.CONFLICT, "Path point already exists"),
    ID_MISSING("2", HttpStatus.BAD_REQUEST, "Id is missing"),
    NOT_FOUND("3", HttpStatus.NOT_FOUND, "Path point not found"),
    PATH_DOES_NOT_EXIST("4", HttpStatus.NOT_FOUND, "Path does not exist"),
    ACCESS_DENIED("5", HttpStatus.FORBIDDEN, "Path not in media paths")
    ;

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String defaultMessage;

    @Override
    public String getErrorCode() {
        return "PathPoint" + errorCode;
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
