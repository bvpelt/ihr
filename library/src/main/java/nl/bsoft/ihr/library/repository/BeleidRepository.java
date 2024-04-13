package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.BeleidDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeleidRepository extends PagingAndSortingRepository<BeleidDto, Long>,
        CrudRepository<BeleidDto, Long>,
        JpaSpecificationExecutor<BeleidDto> {

    Optional<BeleidDto> findByBelangAndRolAndInstrument(String belang, String rol, String instrument);
}
