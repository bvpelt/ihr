package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.OverheidDto;
import nl.bsoft.ihr.library.model.dto.TekstDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OverheidRepository extends PagingAndSortingRepository<OverheidDto, Long>,
        CrudRepository<OverheidDto, Long>,
        JpaSpecificationExecutor<OverheidDto> {

    Optional<OverheidDto> findByNaam(String naam);

    Optional<OverheidDto> findByCode(String code);
}
