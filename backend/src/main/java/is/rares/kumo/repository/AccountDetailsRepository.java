package is.rares.kumo.repository;

import is.rares.kumo.domain.AccountDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailsRepository extends BasePagingAndSortingRepository<AccountDetails> {
}
