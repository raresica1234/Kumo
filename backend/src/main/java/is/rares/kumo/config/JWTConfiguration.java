package is.rares.kumo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JWTConfiguration {
    @Value("${jwt.issuer:Kumo}")
    private String issuer;

    @Value("${jwt.accessTokenValidity:600000}")
    private int accessTokenValidity;

    @Value("${jwt.refreshTokenValidity:86400000}")
    private int refreshTokenValidity;
}
