package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.StructuurVisieGebiedBeleidDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface StructuurvisieGebiedBeleidRepository extends PagingAndSortingRepository<StructuurVisieGebiedBeleidDto, Long>,
        CrudRepository<StructuurVisieGebiedBeleidDto, Long>,
        JpaSpecificationExecutor<StructuurVisieGebiedBeleidDto> {

    Optional<StructuurVisieGebiedBeleidDto> findByBelangAndRolAndInstrument(String belang, String rol, String instrument);
}
