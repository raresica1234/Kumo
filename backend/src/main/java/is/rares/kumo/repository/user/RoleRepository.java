package is.rares.kumo.repository.user;

import is.rares.kumo.domain.user.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends BasePagingAndSortingRepository<Role> {
    Optional<Role> findByName(String owner);

    @Query(value = "select * from kumo_role role inner join user_role on user_role.role_id=role.uuid where user_role.user_id = :userId", nativeQuery = true)
    List<Role> getAllRolesForUser(@QueryParam("userId") UUID userId);
}
