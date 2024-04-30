package is.rares.kumo.core.exceptions.codes;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public interface BaseErrorCode extends Serializable {
    String getErrorCode();

    String getDefaultMessage();

    HttpStatus getHttpStatus();
}
