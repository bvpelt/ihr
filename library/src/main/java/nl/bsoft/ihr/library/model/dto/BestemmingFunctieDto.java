package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "bestemmingsfunctie", schema = "public", catalog = "ihr")
public class BestemmingFunctieDto {
    private static final long serialVersionUID = 13L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bestemmingsfunctie")
    private String bestemmingsfunctie;
    @Column(name = "functieniveau")
    private String functieniveau;

    @ManyToMany
    private Set<BestemmingsvlakDto> bestemmingsvlakken;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BestemmingFunctieDto that = (BestemmingFunctieDto) o;
        return Objects.equals(bestemmingsfunctie, that.bestemmingsfunctie) && Objects.equals(functieniveau, that.functieniveau) && Objects.equals(bestemmingsvlakken, that.bestemmingsvlakken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bestemmingsfunctie, functieniveau, bestemmingsvlakken);
    }

    @Override
    public String toString() {
        return "BestemmingFunctieDto{" +
                "id=" + id +
                ", bestemmingsfunctie='" + bestemmingsfunctie + '\'' +
                ", functieniveau='" + functieniveau + '\'' +
                ", bestemmingsvlakken=" + bestemmingsvlakken +
                '}';
    }
}
