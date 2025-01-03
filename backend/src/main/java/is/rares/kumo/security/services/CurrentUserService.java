package is.rares.kumo.security.services;

import is.rares.kumo.security.domain.CurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public CurrentUser getUser() {
        if (SecurityContextHolder.getContext() == null)
            return null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null)
            return null;

        return (CurrentUser) authentication.getPrincipal();
    }
}


