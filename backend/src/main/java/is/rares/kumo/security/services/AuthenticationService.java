package is.rares.kumo.security.services;

import is.rares.kumo.config.JWTConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService extends KeyLoaderService {
    private final JWTConfiguration jwtConfiguration;

    @Autowired
    public AuthenticationService(JWTConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }
}
