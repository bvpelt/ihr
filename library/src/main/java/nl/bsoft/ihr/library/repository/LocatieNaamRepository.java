package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.LocatieNaamDto;
import nl.bsoft.ihr.library.model.dto.OverheidDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocatieNaamRepository extends PagingAndSortingRepository<LocatieNaamDto, Long>,
        CrudRepository<LocatieNaamDto, Long>,
        JpaSpecificationExecutor<LocatieNaamDto> {

    Optional<LocatieNaamDto> findByNaam(String naam);
}
