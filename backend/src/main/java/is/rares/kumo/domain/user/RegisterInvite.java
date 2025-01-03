package is.rares.kumo.domain.user;

import is.rares.kumo.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "register_invite")
public class RegisterInvite extends BaseEntity {
    @Column(name = "jwt_token", columnDefinition = "TEXT", unique = true)
    private String jwtToken;

    private int usageCount = 0;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime expireDate;

    @Enumerated(EnumType.STRING)
    private RegisterInviteStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegisterInvite that = (RegisterInvite) o;
        return usageCount == that.usageCount && Objects.equals(jwtToken, that.jwtToken) &&
                Objects.equals(createdAt, that.createdAt) && Objects.equals(expireDate, that.expireDate) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), jwtToken, usageCount, createdAt, expireDate, status);
    }
}
