package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.OmvangDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OmvangRepository extends PagingAndSortingRepository<OmvangDto, Long>,
        CrudRepository<OmvangDto, Long>,
        JpaSpecificationExecutor<OmvangDto> {

    Optional<OmvangDto> findByNaamAndWaarde(String naam, String waarde);
}
