package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.LettertekenaanduidingDto;
import nl.bsoft.ihr.library.model.dto.MaatvoeringDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaatvoeringRepository extends PagingAndSortingRepository<MaatvoeringDto, Long>,
        CrudRepository<MaatvoeringDto, Long>,
        JpaSpecificationExecutor<MaatvoeringDto> {

    Optional<MaatvoeringDto> findByPlanidentificatieAndIdentificatie(String planidentificatie, String identificatie);
}
