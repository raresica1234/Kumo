package is.rares.kumo.repository;

import is.rares.kumo.domain.user.Feature;
import is.rares.kumo.domain.user.Authority;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends BasePagingAndSortingRepository<Authority> {
    Optional<Authority> findByFeature(Feature feature);
}
