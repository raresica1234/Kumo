package is.rares.kumo.repository;

import is.rares.kumo.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BasePagingAndSortingRepository<User> {
}
