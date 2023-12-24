package is.rares.kumo.domain.user;

import is.rares.kumo.domain.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authority")
public class Authority extends BaseEntity implements GrantedAuthority {

    private String name;

    private String code;

    @Override
    public String getAuthority() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Authority authority = (Authority) o;
        return uuid != null && Objects.equals(uuid, authority.uuid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
