package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.TekstDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TekstRepository extends PagingAndSortingRepository<TekstDto, Long>,
        CrudRepository<TekstDto, Long>,
        JpaSpecificationExecutor<TekstDto> {

    Optional<TekstDto> findByPlanidentificatieAndTekstidentificatie(String planidentificatie, String tekstIdentificatie);
}
