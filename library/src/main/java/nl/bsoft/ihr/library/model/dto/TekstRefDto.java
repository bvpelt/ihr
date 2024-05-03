package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tekstref", schema = "public", catalog = "ihr")
public class TekstRefDto {
    private static final long serialVersionUID = 11L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "referentie")
    private String referentie;

    @ManyToMany(mappedBy = "verwijzingNaarTekst", fetch = FetchType.LAZY)
    private Set<BestemmingsvlakDto> bestemmingsvlakken = new HashSet<BestemmingsvlakDto>();

    @ManyToMany(mappedBy = "verwijzingNaarTekst", fetch = FetchType.LAZY)
    private Set<GebiedsaanduidingDto> gebiedsaanwijzingen = new HashSet<>();

    @ManyToMany(mappedBy = "verwijzingNaarTekst", fetch = FetchType.LAZY)
    private Set<StructuurVisieGebiedDto> verwijzingNaarTekst;

    @ManyToMany(mappedBy = "verwijzingnaartekst", fetch = FetchType.LAZY)
    private Set<FiguurDto> figuren;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TekstRefDto that = (TekstRefDto) o;
        return Objects.equals(referentie, that.referentie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(referentie);
    }

    @Override
    public String toString() {
        return "TekstRefDto{" +
                "id=" + id +
                ", referentie='" + referentie + '\'' +
                '}';
    }
}
