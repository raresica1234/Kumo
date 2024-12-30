package is.rares.kumo.repository.explore;

import is.rares.kumo.domain.explore.ExplorationRole;
import is.rares.kumo.repository.user.BasePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExplorationRoleRepository extends BasePagingAndSortingRepository<ExplorationRole> {
    Optional<ExplorationRole> findByName(String name);

    Page<ExplorationRole> findByNameContainsIgnoreCase(String name, Pageable pageable);
}
