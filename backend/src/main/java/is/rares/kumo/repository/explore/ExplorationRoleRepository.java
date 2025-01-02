package is.rares.kumo.repository.explore;

import is.rares.kumo.domain.explore.ExplorationRole;
import is.rares.kumo.repository.user.BasePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExplorationRoleRepository extends BasePagingAndSortingRepository<ExplorationRole> {
    Optional<ExplorationRole> findByName(String name);

    Page<ExplorationRole> findByNameContainsIgnoreCase(String name, Pageable pageable);

    List<ExplorationRole> findByUsers_Uuid(UUID uuid);
}
