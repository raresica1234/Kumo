package is.rares.kumo.repository;

import is.rares.kumo.domain.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BasePagingAndSortingRepository<Role> {
}
