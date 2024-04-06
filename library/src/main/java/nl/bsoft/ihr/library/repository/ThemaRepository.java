package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.ThemaDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThemaRepository extends PagingAndSortingRepository<ThemaDto, Long>,
        CrudRepository<ThemaDto, Long>,
        JpaSpecificationExecutor<ThemaDto> {

    Optional<ThemaDto> findByThema(String thema);
}
