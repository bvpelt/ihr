package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

    @ManyToOne
    @JoinColumn(name="structuurvisiegebied_id", nullable = false)
    private StructuurVisieGebiedDto structuurvisiegebied;
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
