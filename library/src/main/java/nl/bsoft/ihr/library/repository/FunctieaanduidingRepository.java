package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.BouwvlakDto;
import nl.bsoft.ihr.library.model.dto.FunctieaanduidingDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FunctieaanduidingRepository extends PagingAndSortingRepository<FunctieaanduidingDto, Long>,
        CrudRepository<FunctieaanduidingDto, Long>,
        JpaSpecificationExecutor<FunctieaanduidingDto> {

    Optional<FunctieaanduidingDto> findByPlanidentificatieAndIdentificatie(String planidentificatie, String identificatie);
}
