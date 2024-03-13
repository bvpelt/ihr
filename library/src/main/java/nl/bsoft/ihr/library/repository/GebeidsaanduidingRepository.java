package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.GebiedsaanduidingDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GebeidsaanduidingRepository extends PagingAndSortingRepository<GebiedsaanduidingDto, Long>,
        CrudRepository<GebiedsaanduidingDto, Long>,
        JpaSpecificationExecutor<GebiedsaanduidingDto> {

    Optional<GebiedsaanduidingDto> findByIdentificatie(String identificatie);
}