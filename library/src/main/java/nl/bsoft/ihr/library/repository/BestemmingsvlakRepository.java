package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.BestemmingsvlakDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BestemmingsvlakRepository extends PagingAndSortingRepository<BestemmingsvlakDto, Long>,
        CrudRepository<BestemmingsvlakDto, Long>,
        JpaSpecificationExecutor<BestemmingsvlakDto> {

    Optional<BestemmingsvlakDto> findByPlanidentificatieAndTekstidentificatie(String planidentificatie, String tekstIdentificatie);
}
