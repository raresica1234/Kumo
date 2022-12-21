package is.rares.kumo.core.exceptions.handlers;

import is.rares.kumo.core.exceptions.ErrorResponse;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Locale;
import java.util.Optional;

@ControllerAdvice
@Configuration
@EnableWebMvc
public class CustomExceptionHandler {

    private final MessageSource messageSource;


    @Autowired
    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(KumoException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(KumoException exception, Locale locale) {
        String errorMessage;
        if (exception.getCustomMessage() != null && !exception.getCustomMessage().isEmpty())
            errorMessage = exception.getCustomMessage();
        else
            errorMessage = messageSource.getMessage(exception.getErrorCode().getMessageKey(), null, locale);

        return new ResponseEntity<>(new ErrorResponse(exception, errorMessage), exception.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValidException(MethodArgumentNotValidException exception, Locale locale) {
        BindingResult result = exception.getBindingResult();
        String errorMessage = result.getAllErrors()
                .stream()
                .map(objectError -> messageSource.getMessage(objectError, locale))
                .reduce("", (str1, str2) -> str2 + "\n" + str1);

        if (!errorMessage.isEmpty())
            return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.BAD_REQUEST);
        else {
            String defaultErrorMessage = messageSource.getMessage(exception.getMessage(), null, locale);
            return new ResponseEntity<>(new ErrorResponse(AccountCodeErrorCodes.UNEXPECTED_ERROR, defaultErrorMessage), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception, Locale locale) {
        String errorMessage = messageSource.getMessage(AccountCodeErrorCodes.UNEXPECTED_ERROR.getMessageKey(), null, locale);
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse(AccountCodeErrorCodes.UNEXPECTED_ERROR, errorMessage), AccountCodeErrorCodes.UNEXPECTED_ERROR.getHttpStatus());
    }
}

