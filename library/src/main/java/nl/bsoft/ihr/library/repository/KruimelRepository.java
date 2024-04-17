package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.KruimelDto;
import nl.bsoft.ihr.library.model.dto.OverheidDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KruimelRepository extends PagingAndSortingRepository<KruimelDto, Long>,
        CrudRepository<KruimelDto, Long>,
        JpaSpecificationExecutor<KruimelDto> {

    Optional<KruimelDto> findByIdentificatieAndTitelAndVolgnummer(String identificatie, String titel, Integer volgnummer);

}
