package is.rares.kumo.repository.explore;

import is.rares.kumo.domain.explore.PathPoint;
import is.rares.kumo.repository.user.BasePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PathPointRepository extends BasePagingAndSortingRepository<PathPoint> {
    Optional<PathPoint> findByPathAndRoot(String path, boolean root);

    Page<PathPoint> findByPathContainsIgnoreCase(String path, Pageable pageable);
}
