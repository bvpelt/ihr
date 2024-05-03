package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.VerwijzingNormDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerwijzingNormRepository extends PagingAndSortingRepository<VerwijzingNormDto, Long>,
        CrudRepository<VerwijzingNormDto, Long>,
        JpaSpecificationExecutor<VerwijzingNormDto> {

    Optional<VerwijzingNormDto> findByNorm(String norm);
}
