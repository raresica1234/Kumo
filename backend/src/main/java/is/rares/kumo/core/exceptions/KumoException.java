package is.rares.kumo.core.exceptions;

import is.rares.kumo.core.exceptions.codes.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KumoException extends RuntimeException {
    private BaseErrorCode errorCode;

    private String customMessage;

    public KumoException(BaseErrorCode errorCode) {
        this.errorCode = errorCode;
        this.customMessage = null;
    }
}
