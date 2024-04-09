package is.rares.kumo.repository;

import is.rares.kumo.domain.user.RegisterInvite;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterInviteRepository extends BasePagingAndSortingRepository<RegisterInvite>{
    Optional<RegisterInvite> findByJwtToken(String jwtToken);
}
