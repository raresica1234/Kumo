package is.rares.kumo.repository;

import is.rares.kumo.domain.user.Authority;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends BasePagingAndSortingRepository<Authority> {
}
