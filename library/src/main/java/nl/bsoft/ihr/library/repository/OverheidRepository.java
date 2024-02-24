package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.OverheidDto;
import nl.bsoft.ihr.library.model.dto.PlanDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OverheidRepository extends PagingAndSortingRepository<OverheidDto, Long>,
        CrudRepository<OverheidDto, Long>,
        JpaSpecificationExecutor<OverheidDto> {

    @Query(
            value =
                    "SELECT * FROM overheid WHERE plan_identificatie = :identificatie and code = :code", nativeQuery = true)
    Optional<OverheidDto> findByIdentificatieAndCode(String identificatie, String code);
}