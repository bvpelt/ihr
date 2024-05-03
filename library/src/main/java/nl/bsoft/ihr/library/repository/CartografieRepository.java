package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.CartografieInfoDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartografieRepository extends PagingAndSortingRepository<CartografieInfoDto, Long>,
        CrudRepository<CartografieInfoDto, Long>,
        JpaSpecificationExecutor<CartografieInfoDto> {

    Optional<CartografieInfoDto> findByKaartnummerAndKaartnaamAndSymboolcode(Integer kaartnummer, String Kaartnaam, String symboolcode);
}
