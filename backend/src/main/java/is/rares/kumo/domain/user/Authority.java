package is.rares.kumo.domain.user;

import is.rares.kumo.domain.BaseEntity;
import is.rares.kumo.domain.enums.AuthorityType;
import is.rares.kumo.domain.enums.Feature;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

// TODO: Why is this a table bruh
public class Authority extends BaseEntity implements GrantedAuthority {

    @Enumerated(EnumType.STRING)
    private AuthorityType type;

    @Enumerated(EnumType.STRING)
    private Feature feature;

    @Override
    public String getAuthority() {
        return feature.getName();
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
