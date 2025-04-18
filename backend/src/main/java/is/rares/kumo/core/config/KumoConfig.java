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

    @Value("${kumo.owner.username:}") // null default
    String ownerUsername;

    @Value("${kumo.owner.password:}") // null default
    String ownerPassword;

    @Value("${kumo.owner.email:}") // null default
    String ownerEmail;

    @Value("${kumo.thumbnailDirectory:/data}")
    String thumbnailDirectoryPath;

    @Value("${kumo.mediaPath:/}")
    String mediaPath;
}
