package is.rares.kumo.repository.user;

import is.rares.kumo.domain.user.RegisterInvite;
import is.rares.kumo.domain.user.RegisterInviteStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.QueryParam;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegisterInviteRepository extends BasePagingAndSortingRepository<RegisterInvite>{
    Optional<RegisterInvite> findByJwtToken(String jwtToken);

    @Transactional
    @Modifying
    @Query("update RegisterInvite r set r.usageCount = :usageCount, r.status = :status where r.uuid = :uuid")
    void updateUsageCountAndStatusByUuid(@QueryParam("uuid") UUID uuid, @QueryParam("usageCount") int usageCount,
                                         @QueryParam("status") RegisterInviteStatus status);
}
