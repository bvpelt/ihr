package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.TekstDto;
import nl.bsoft.ihr.library.model.dto.TekstRefDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TekstRefRepository extends PagingAndSortingRepository<TekstRefDto, Long>,
        CrudRepository<TekstRefDto, Long>,
        JpaSpecificationExecutor<TekstRefDto> {

}
