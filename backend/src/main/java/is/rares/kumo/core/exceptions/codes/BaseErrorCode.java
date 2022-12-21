package is.rares.kumo.core.exceptions.codes;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    String getErrorCode();

    String getMessageKey();

    HttpStatus getHttpStatus();
}
