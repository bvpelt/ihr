package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.AuditLogDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends PagingAndSortingRepository<AuditLogDto, Long>,
        CrudRepository<AuditLogDto, Long>,
        JpaSpecificationExecutor<AuditLogDto> {

}