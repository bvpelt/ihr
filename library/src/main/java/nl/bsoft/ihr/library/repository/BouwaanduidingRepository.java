package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.BouwaanduidingDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BouwaanduidingRepository extends PagingAndSortingRepository<BouwaanduidingDto, Long>,
        CrudRepository<BouwaanduidingDto, Long>,
        JpaSpecificationExecutor<BouwaanduidingDto> {

    Optional<BouwaanduidingDto> findByPlanidentificatieAndIdentificatie(String planidentificatie, String identificatie);
}
