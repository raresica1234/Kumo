package is.rares.kumo.security.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CurrentUser extends User {

    private UUID id;
    private Date expirationDate;
    private boolean isUsing2FA;
    private boolean twoFANeeded;


    public CurrentUser(UUID id,
                       String username,
                       String password,
                       Collection<? extends GrantedAuthority> authorities,
                       boolean isUsing2FA,
                       boolean twoFANeeded,
                       Date expirationDate) {
        super(username, password, authorities);
        this.id = id;
        this.expirationDate = expirationDate;
        this.isUsing2FA = isUsing2FA;
        this.twoFANeeded = twoFANeeded;
    }

}
