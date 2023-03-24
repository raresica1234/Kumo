package is.rares.kumo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BasePagingAndSortingRepository<T> extends PagingAndSortingRepository<T, UUID>, CrudRepository<T, UUID> {
    Optional<T> findByUuid(UUID id);
}
