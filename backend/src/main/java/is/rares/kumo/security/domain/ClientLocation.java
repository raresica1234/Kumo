package is.rares.kumo.security.domain;

import is.rares.kumo.domain.BaseEntity;
import is.rares.kumo.security.enums.ClientLocationType;
import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client_location")
public class ClientLocation extends BaseEntity {
    @Column
    String country;

    @Column
    String ipAddress;

    @Enumerated(EnumType.STRING)
    ClientLocationType locationType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ClientLocation that = (ClientLocation) o;
        return uuid != null && Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
