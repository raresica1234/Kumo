package is.rares.kumo.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class KumoConfig {
    @Value("${kumo.registration.inviteBased:false}")
    boolean inviteBasedRegistration;

    @Value("${kumo.registration.requireAccountVerification:false}")
    boolean requireAccountVerification;
}
