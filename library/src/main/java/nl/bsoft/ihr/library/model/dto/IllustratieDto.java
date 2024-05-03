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
@Table(name = "illustratie", schema = "public", catalog = "ihr")
public class IllustratieDto {
    private static final long serialVersionUID = 16L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "href")
    private String href;
    @Column(name = "type")
    private String type;
    @Column(name = "naam")
    private String naam;
    @Column(name = "legendanaam")
    private String legendanaam;
    @ManyToMany(mappedBy = "illustraties", fetch = FetchType.LAZY)
    private Set<StructuurVisieGebiedDto> structuurvisiegebied = new HashSet<>();
    @ManyToMany(mappedBy = "illustraties", fetch = FetchType.LAZY)
    private Set<FiguurDto> figuren = new HashSet<>();
    @ManyToOne()
    @JoinColumn(name = "plan_id", nullable = true, referencedColumnName = "id")
    private PlanDto plan;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IllustratieDto that = (IllustratieDto) o;
        return Objects.equals(href, that.href) && Objects.equals(type, that.type) && Objects.equals(naam, that.naam) && Objects.equals(legendanaam, that.legendanaam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(href, type, naam, legendanaam);
    }

    @Override
    public String toString() {
        return "IllustratieDto{" +
                "id=" + id +
                ", href='" + href + '\'' +
                ", type='" + type + '\'' +
                ", naam='" + naam + '\'' +
                ", legendanaam='" + legendanaam + '\'' +
                '}';
    }
}
