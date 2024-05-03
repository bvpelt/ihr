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
@Table(name = "artikel", schema = "public", catalog = "ihr")
public class ArtikelDto {
    private static final long serialVersionUID = 12L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "artikel")
    private String artikel;

    @ManyToMany(mappedBy = "artikelnummers", fetch = FetchType.LAZY)
    private Set<GebiedsaanduidingDto> gebiedsaanduidingen = new HashSet<GebiedsaanduidingDto>();

    @ManyToMany(mappedBy = "artikelnummers", fetch = FetchType.LAZY)
    private Set<FiguurDto> figuren = new HashSet<FiguurDto>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtikelDto that = (ArtikelDto) o;
        return Objects.equals(artikel, that.artikel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artikel);
    }

    @Override
    public String toString() {
        return "ArtikelnummerRefDto{" +
                "id=" + id +
                ", artikel='" + artikel + '\'' +
                '}';
    }
}
