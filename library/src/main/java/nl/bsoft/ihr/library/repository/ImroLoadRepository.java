package nl.bsoft.ihr.library.repository;

import nl.bsoft.ihr.library.model.dto.ImroLoadDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImroLoadRepository extends PagingAndSortingRepository<ImroLoadDto, Long>,
        CrudRepository<ImroLoadDto, Long>,
        JpaSpecificationExecutor<ImroLoadDto> {

    @Query(
            value =
                    "SELECT * FROM imroload WHERE  loaded = false", nativeQuery = true)
    Iterable<ImroLoadDto> findByIdentificatieNotLoaded();

    @Query(
            value =
                    "SELECT * FROM imroload WHERE  tekstentried = false", nativeQuery = true)
    Iterable<ImroLoadDto> findByTekstenNotTried();

    @Query(
            value =
                    "SELECT * FROM imroload WHERE  bestemmingsvlakkentried = false", nativeQuery = true)
    Iterable<ImroLoadDto> findByBestemmingsvlakkenNotTried();

    @Query(
            value =
                    "SELECT * FROM imroload WHERE  structuurvisiegebiedtried = false", nativeQuery = true)
    Iterable<ImroLoadDto> findByStructuurvisiegebiedNotTried();
    @Query(
            value =
                    "SELECT * FROM imroload WHERE  bouwvlakkentried = false", nativeQuery = true)
    Iterable<ImroLoadDto> findByBouwvlakkenNotTried();
    @Query(
            value =
                    "SELECT * FROM imroload WHERE  functieaanduidingtried = false", nativeQuery = true)
    Iterable<ImroLoadDto> findByFunctieaanduidingNotTried();

    @Query(
            value =
                    "SELECT * FROM imroload WHERE  bouwaanduidingtried = false", nativeQuery = true)
    Iterable<ImroLoadDto> findByBouwaanduidingNotTried();

    @Query(
            value =
                    "SELECT * FROM imroload WHERE  lettertekenaanduidingtried = false", nativeQuery = true)
    Iterable<ImroLoadDto> findByLettertekenaanduidingNotTried();

    @Query(
            value =
                    "SELECT * FROM imroload WHERE  maatvoeringtried = false", nativeQuery = true)
    Iterable<ImroLoadDto> findByMaatvoeringNotTried();


    @Query(
            value =
                    "SELECT * FROM imroload WHERE  figuurtried = false", nativeQuery = true)
    Iterable<ImroLoadDto> findByFiguurNotTried();
}
