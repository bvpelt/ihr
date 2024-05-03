package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.NormadressantDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NormadressantRepository extends PagingAndSortingRepository<NormadressantDto, Long>,
        CrudRepository<NormadressantDto, Long>,
        JpaSpecificationExecutor<NormadressantDto> {

    Optional<NormadressantDto> findByNorm(String norm);
}