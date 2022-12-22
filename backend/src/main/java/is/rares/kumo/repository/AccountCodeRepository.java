package is.rares.kumo.repository;

import is.rares.kumo.domain.AccountCode;
import is.rares.kumo.domain.enums.AccountCodeStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

@Repository
public interface AccountCodeRepository extends BasePagingAndSortingRepository<AccountCode> {

    List<AccountCode> findAllByUserId(UUID userId);

    @Modifying
    @Query("update AccountCode code set code.codeStatus = :codeStatus where code.uuid = :uuid")
    void updateAccountCodeStatus(@QueryParam("uuid") UUID uuid, @QueryParam("codeStatus") AccountCodeStatus codeStatus);
}
