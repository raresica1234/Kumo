package is.rares.kumo.domain.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import is.rares.kumo.domain.BaseEntity;
import lombok.Data;

import jakarta.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "kumo_role")
public class Role extends BaseEntity {

    String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_authority",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "uuid")
    )
    Set<Authority> authorities;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    )
    @JsonIgnore
    Set<User> users;
}
