package is.rares.kumo.domain;


import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="kumo_role")
public class Role extends BaseEntity {

}
