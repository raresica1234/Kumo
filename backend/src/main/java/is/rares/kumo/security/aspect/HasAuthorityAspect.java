package is.rares.kumo.security.aspect;

import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AuthorizationErrorCodes;
import is.rares.kumo.domain.user.AuthorityType;
import is.rares.kumo.domain.user.Feature;
import is.rares.kumo.repository.RoleRepository;
import is.rares.kumo.security.annotation.HasAuthority;
import is.rares.kumo.security.domain.CurrentUser;
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
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class HasAuthorityAspect {

    private final RoleRepository roleRepository;

    public HasAuthorityAspect(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Before("@annotation(is.rares.kumo.security.annotation.HasAuthority)")
    public void hasTokenTypeCheck(final JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new AuthenticationCredentialsNotFoundException("An Authentication object was not found in the SecurityContext");

        if (!(authentication.getPrincipal() instanceof CurrentUser currentUser))
            throw new KumoException(AuthorizationErrorCodes.INVALID_TOKEN, "Invalid token");

        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final HasAuthority hasAuthorityAnnotation = AnnotationUtils.findAnnotation(method, HasAuthority.class);
        if (hasAuthorityAnnotation != null) {
            Set<Feature> userFeatures = getAllUserFeatures(currentUser);
            if (userFeatures.contains(Feature.OWNER)) return;

            boolean missingAuthority = false;

            for (Feature feature : hasAuthorityAnnotation.value()) {
                if (!userFeatures.contains(feature)) {
                    missingAuthority = true;
                    break;
                }
            }

            if (missingAuthority) throw new KumoException(AuthorizationErrorCodes.UNAUTHORIZED, "Access is denied");
        }
    }

    Set<Feature> getAllUserFeatures(CurrentUser currentUser) {
        return roleRepository.getAllRolesForUser(currentUser.getId()).stream()
                .flatMap(role -> role.getFeatures().stream())
                .collect(Collectors.toSet());
    }

}
