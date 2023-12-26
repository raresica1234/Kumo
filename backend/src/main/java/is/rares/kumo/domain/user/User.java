package is.rares.kumo.domain.user;

import is.rares.kumo.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "kumo_user")
public class User extends BaseEntity implements UserDetails {
    @Column(unique = true)
    String username;

    @Column(unique = true)
    String email;

    @Column
    String password;

    boolean isUsing2FA;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "detail_id")
    private AccountDetails accountDetails = new AccountDetails();

    @Column(name = "detail_id", insertable = false, updatable = false)
    private UUID detailId;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Authority> authorities = new HashSet<>();
        if (roles != null) {
            return roles.stream()
                    .flatMap(role -> role.getAuthorities().stream())
                    .filter(authority -> authority.getType().equals(AuthorityType.FEATURE))
                    .collect(Collectors.toSet());
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return uuid != null && Objects.equals(uuid, user.uuid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}