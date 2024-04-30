package is.rares.kumo.domain.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import is.rares.kumo.domain.BaseEntity;
import is.rares.kumo.security.enums.Feature;
import lombok.*;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "kumo_role")
public class Role extends BaseEntity {

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private Set<Feature> features = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    )
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name) && Objects.equals(features, role.features) && Objects.equals(users, role.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, features, users);
    }
}
