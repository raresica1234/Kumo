package is.rares.kumo.domain.explore;

import is.rares.kumo.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "path_point")
public class PathPoint extends BaseEntity {
    @Column
    String path;

    @Column
    boolean root;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PathPoint pathPoint = (PathPoint) o;
        return root == pathPoint.root && Objects.equals(path, pathPoint.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), path, root);
    }
}
