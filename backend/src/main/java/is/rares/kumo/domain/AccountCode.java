package is.rares.kumo.domain;

import is.rares.kumo.domain.enums.AccountCodeStatus;
import is.rares.kumo.domain.enums.AccountCodeType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "account_code")
public class AccountCode extends BaseEntity {
    private String code;

    private AccountCodeType codeType;
    private AccountCodeStatus codeStatus;

    @CreatedDate
    private LocalDateTime creationDate;

    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "uuid")
    private User user;

    @Column(name ="user_id", insertable = false, updatable = false)
    private UUID userId;
}
