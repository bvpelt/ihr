package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.ArtikelDto;
import nl.bsoft.ihr.library.model.dto.AuditLogDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtikelRepository extends PagingAndSortingRepository<ArtikelDto, Long>,
        CrudRepository<ArtikelDto, Long>,
        JpaSpecificationExecutor<ArtikelDto> {

    Optional<ArtikelDto> findByArtikel(String artikel);
}
