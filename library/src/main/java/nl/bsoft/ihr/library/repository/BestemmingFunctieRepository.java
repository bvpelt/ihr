package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.BestemmingFunctieDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BestemmingFunctieRepository extends PagingAndSortingRepository<BestemmingFunctieDto, Long>,
        CrudRepository<BestemmingFunctieDto, Long>,
        JpaSpecificationExecutor<BestemmingFunctieDto> {

    Optional<BestemmingFunctieDto> findByBestemmingsfunctieAndFunctieniveau(String bestemmingsfunctie, String functieniveau);
}
