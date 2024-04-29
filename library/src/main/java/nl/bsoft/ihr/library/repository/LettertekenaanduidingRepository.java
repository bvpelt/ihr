package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.LettertekenaanduidingDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LettertekenaanduidingRepository extends PagingAndSortingRepository<LettertekenaanduidingDto, Long>,
        CrudRepository<LettertekenaanduidingDto, Long>,
        JpaSpecificationExecutor<LettertekenaanduidingDto> {

    Optional<LettertekenaanduidingDto> findByPlanidentificatieAndIdentificatie(String planidentificatie, String identificatie);
}
