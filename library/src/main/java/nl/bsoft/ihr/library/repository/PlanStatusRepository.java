package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.generated.model.PlanstatusInfo;
import nl.bsoft.ihr.library.model.dto.PlanDto;
import nl.bsoft.ihr.library.model.dto.PlanStatusDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PlanStatusRepository extends PagingAndSortingRepository<PlanStatusDto, Long>,
        CrudRepository<PlanStatusDto, Long>,
        JpaSpecificationExecutor<PlanStatusDto> {

    Optional<PlanStatusDto> findByStatusAndDatum(String value, LocalDate datum);
}