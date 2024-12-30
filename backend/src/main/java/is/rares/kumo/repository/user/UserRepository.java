package is.rares.kumo.repository.user;

import is.rares.kumo.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BasePagingAndSortingRepository<User> {

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> findByUsernameContainsIgnoreCaseAndExplorationRoles_NameContainsIgnoreCase(String username, String name, Pageable pageable);

    Page<User> findByUsernameContainsIgnoreCase(String username, Pageable pageable);

    boolean existsByUuidAndExplorationRoles_Uuid(UUID user, UUID explorationRole);

    @Modifying
    @Query(nativeQuery = true, value = "delete from user_explore_role where user_id=:user and explore_role_id=:role")
    void deleteUserExplorationRole(@Param("user") UUID user, @Param("role") UUID explorationRole);

    @Modifying
    @Query(nativeQuery = true, value = "delete from user_explore_role where user_id=:user")
    void deleteAllExplorationRolesForUser(@Param("user") UUID user);

    @Modifying
    @Query(nativeQuery = true, value = "insert into user_explore_role (explore_role_id, user_id) values (:roleId, :userId) ")
    void addUserExplorationRole(@Param("userId") UUID userId, @Param("roleId") UUID roleId);
}
