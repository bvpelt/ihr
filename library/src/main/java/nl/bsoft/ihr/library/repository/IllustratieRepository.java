package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.IllustratieDto;
import nl.bsoft.ihr.library.model.dto.LocatieDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IllustratieRepository extends PagingAndSortingRepository<IllustratieDto, Long>,
        CrudRepository<IllustratieDto, Long>,
        JpaSpecificationExecutor<IllustratieDto> {

    Optional<IllustratieDto> findByHrefAndTypeAndNaamAndLegendanaam(String href, String type, String naam, String legendanaam);
}
