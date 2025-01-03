package is.rares.kumo.domain.explore;

import is.rares.kumo.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permission")
public class Permission extends BaseEntity {
    @Column
    boolean read;

    @Column
    boolean write;

    @Column
    boolean delete;

    @Column
    boolean modifyRoot;

    @ManyToOne
    @JoinColumn(name = "path_point_id")
    private PathPoint pathPoint;

    @Column(name = "path_point_id", insertable = false, updatable = false)
    private UUID pathPointId;

    @ManyToOne
    @JoinColumn(name = "exploration_role_id")
    private ExplorationRole explorationRole;

    @Column(name = "exploration_role_id", insertable = false, updatable = false)
    private UUID explorationRoleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Permission that = (Permission) o;
        return read == that.read && write == that.write && delete == that.delete && modifyRoot == that.modifyRoot &&
                Objects.equals(pathPoint, that.pathPoint) && Objects.equals(pathPointId, that.pathPointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), read, write, delete, modifyRoot, pathPoint, pathPointId);
    }
}

