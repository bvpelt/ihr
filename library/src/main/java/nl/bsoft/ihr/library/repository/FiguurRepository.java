package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.FiguurDto;
import nl.bsoft.ihr.library.model.dto.MaatvoeringDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FiguurRepository extends PagingAndSortingRepository<FiguurDto, Long>,
        CrudRepository<FiguurDto, Long>,
        JpaSpecificationExecutor<FiguurDto> {

    Optional<FiguurDto> findByPlanidentificatieAndIdentificatie(String planidentificatie, String identificatie);
}
