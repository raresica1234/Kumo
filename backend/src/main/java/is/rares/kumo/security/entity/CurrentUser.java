package is.rares.kumo.security.entity;

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


    public CurrentUser(String username,
                       String password,
                       Collection<? extends GrantedAuthority> authorities,
                       UUID id,
                       Date expirationDate,
                       boolean isUsing2FA,
                       boolean twoFANeeded) {
        super(username, password, authorities);
        this.id = id;
        this.expirationDate = expirationDate;
        this.isUsing2FA = isUsing2FA;
        this.twoFANeeded = twoFANeeded;
    }

}
