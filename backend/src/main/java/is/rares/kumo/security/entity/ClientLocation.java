package is.rares.kumo.security.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import is.rares.kumo.domain.BaseEntity;
import is.rares.kumo.security.enums.ClientLocationType;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Specifies a client location")
@Table(name = "client_location")
public class ClientLocation extends BaseEntity {
    @Column
    @NotNull
    @ApiModelProperty(notes = "Client country")
    String country;

    @Column
    @NotNull
    @ApiModelProperty(notes = "Client ip address")
    String ipAddress;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Client location type")
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
