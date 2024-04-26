package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.BestemmingsvlakDto;
import nl.bsoft.ihr.library.model.dto.BouwvlakDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BouwvlakRepository extends PagingAndSortingRepository<BouwvlakDto, Long>,
        CrudRepository<BouwvlakDto, Long>,
        JpaSpecificationExecutor<BouwvlakDto> {

    Optional<BouwvlakDto> findByPlanidentificatieAndIdentificatie(String planidentificatie, String identificatie);
}
