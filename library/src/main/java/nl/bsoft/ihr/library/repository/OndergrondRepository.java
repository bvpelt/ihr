package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.OndergrondDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OndergrondRepository extends PagingAndSortingRepository<OndergrondDto, Long>,
        CrudRepository<OndergrondDto, Long>,
        JpaSpecificationExecutor<OndergrondDto> {

    Optional<OndergrondDto> findByTypeAndDatum(String type, String datum);
}