package is.rares.kumo.domain;

import javax.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    UUID id;
}
