package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.StructuurVisieGebiedThemaDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StructuurvisieGebiedThemaRepository extends PagingAndSortingRepository<StructuurVisieGebiedThemaDto, Long>,
        CrudRepository<StructuurVisieGebiedThemaDto, Long>,
        JpaSpecificationExecutor<StructuurVisieGebiedThemaDto> {
}
