package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "tekstref", schema = "public", catalog = "ihr")
public class TekstRefDto {
    private static final long serialVersionUID = 11L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "referentie")
    private String referentie;

    @ManyToMany(mappedBy = "verwijzingNaarTekst")
    private Set<BestemmingsvlakDto> bestemmingsvlakken = new HashSet<BestemmingsvlakDto>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TekstRefDto that = (TekstRefDto) o;
        return Objects.equals(referentie, that.referentie) && Objects.equals(bestemmingsvlakken, that.bestemmingsvlakken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(referentie, bestemmingsvlakken);
    }

    @Override
    public String toString() {
        return "TekstRefDto{" +
                "id=" + id +
                ", referentie='" + referentie + '\'' +
                ", bestemmingsvlakken=" + bestemmingsvlakken +
                '}';
    }
}
