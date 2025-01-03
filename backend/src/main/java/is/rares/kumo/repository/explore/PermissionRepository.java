package is.rares.kumo.repository.explore;

import is.rares.kumo.domain.explore.Permission;
import is.rares.kumo.repository.user.BasePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PermissionRepository extends BasePagingAndSortingRepository<Permission> {
    boolean existsByPathPoint_UuidAndExplorationRole_Uuid(UUID pathPointId, UUID explorationRoleId);

    @Query("select perm from Permission perm where lower(perm.explorationRole.name) like lower(concat('%', " + ":name, " + "'%')) and lower(perm.pathPoint.path) like lower(concat('%', :path, '%'))")
    Page<Permission> findByPathAndRole(@Param("path") String path, @Param("name") String name, Pageable pageable);


}
