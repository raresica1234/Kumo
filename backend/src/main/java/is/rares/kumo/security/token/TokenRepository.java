package is.rares.kumo.security.token;

import is.rares.kumo.repository.user.BasePagingAndSortingRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends BasePagingAndSortingRepository<Token> {
    List<Token> findByUserId(UUID userId);

    @Query("update Token t set t.lastActivityDate = :date where t.jwtToken = :jwtToken")
    @Modifying
    void updateTokenUsage(@Param("date") Date date, @Param("jwtToken") String jwtToken);

    Optional<Token> findByRefreshToken(String refreshToken);

    Optional<Token> findByJwtToken(String jwtToken);

    @Transactional
    @Modifying
    @Query(value = "update Token set client_location_id = :clientLocationId where uuid = :uuid", nativeQuery =
            true)
    void updateClientLocationIdByUuid(@Param("uuid") UUID uuid, @Param("clientLocationId") UUID clientLocationId);
}
