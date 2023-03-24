package is.rares.kumo.security.token;

import is.rares.kumo.domain.BaseEntity;
import is.rares.kumo.security.entity.ClientLocation;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Table(name = "token")
public class Token extends BaseEntity {
    @Column(name="jwt_token", columnDefinition = "TEXT")
    String jwtToken;

    @Column(name="refresh_token", columnDefinition = "TEXT")
    String refreshToken;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_location_id")
    ClientLocation clientLocation;

    @Column(name="client_location_id", insertable = false, updatable = false)
    UUID clientLocationId;

    Date lastActivityDate;

    UUID userId;

    int validityMs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Token token = (Token) o;
        return uuid != null && Objects.equals(uuid, token.uuid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

