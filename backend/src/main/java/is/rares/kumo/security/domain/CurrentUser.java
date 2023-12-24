package is.rares.kumo.security.domain;

import is.rares.kumo.security.enums.TokenType;
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
    private TokenType tokenType;

    public CurrentUser(UUID id,
                       String username,
                       String password,
                       Collection<? extends GrantedAuthority> authorities,
                       TokenType tokenType,
                       Date expirationDate) {
        super(username, password, authorities);
        this.id = id;
        this.expirationDate = expirationDate;
        this.tokenType = tokenType;
    }

}
