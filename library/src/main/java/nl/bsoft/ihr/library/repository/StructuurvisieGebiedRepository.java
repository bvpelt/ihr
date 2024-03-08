package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.StructuurVisieGebiedDto;
import nl.bsoft.ihr.library.model.dto.TekstDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StructuurvisieGebiedRepository extends PagingAndSortingRepository<StructuurVisieGebiedDto, Long>,
        CrudRepository<StructuurVisieGebiedDto, Long>,
        JpaSpecificationExecutor<StructuurVisieGebiedDto> {
}
