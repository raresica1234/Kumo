package is.rares.kumo.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JWTConfiguration {
    @Value("${jwt.issuer:Kumo}")
    private String issuer;

    @Value("${jwt.accessTokenValidity:600000}")
    private long accessTokenValidity;

    @Value("${jwt.refreshTokenValidity:86400000}")
    private long refreshTokenValidity;
}
