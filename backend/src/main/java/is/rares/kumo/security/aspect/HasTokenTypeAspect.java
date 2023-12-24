package is.rares.kumo.security.aspect;

import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AuthorizationErrorCodes;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.security.domain.CurrentUser;
import is.rares.kumo.security.enums.TokenType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class HasTokenTypeAspect {

    @Before("@annotation(is.rares.kumo.security.annotation.HasTokenType)")
    public void hasTokenTypeCheck(final JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new AuthenticationCredentialsNotFoundException("An Authentication object was not found in the SecurityContext");

        if (!(authentication.getPrincipal() instanceof CurrentUser currentUser))
            throw new KumoException(AuthorizationErrorCodes.INVALID_TOKEN, "Invalid token");

        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final HasTokenType hasTokenTypeAnnotation = AnnotationUtils.findAnnotation(method, HasTokenType.class);
        if (hasTokenTypeAnnotation != null) {
            boolean found = false;
            for (TokenType tokenType : hasTokenTypeAnnotation.value())
                if (tokenType.equals(currentUser.getTokenType()))
                    found = true;

            if (!found) {
                throw new KumoException(AuthorizationErrorCodes.UNAUTHORIZED, "Access is denied");
            }
        }

    }

}
