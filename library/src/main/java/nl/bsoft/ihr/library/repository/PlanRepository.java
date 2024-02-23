package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.PlanDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends PagingAndSortingRepository<PlanDto, Long>,
        CrudRepository<PlanDto, Long>,
        JpaSpecificationExecutor<PlanDto> {

    @Query(
            value =
                    "SELECT * FROM plan WHERE identificatie = :identificatie", nativeQuery = true)
    Optional<PlanDto> findByIdentificatie(String identificatie);
}