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
@Table(name = "structuurvisiegebied", schema = "public", catalog = "ihr")
public class StructuurVisieGebiedDto {
    private static final long serialVersionUID = 8L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "planidentificatie")
    private String planidentificatie;
    @Column(name = "identificatie")
    private String identificatie;

    @Column(name = "naam")
    private String naam;

    @OneToMany(mappedBy = "structuurvisiegebied" )
    private Set<StructuurVisieGebiedThemaDto> thema;

    @OneToMany(mappedBy="structuurvisiegebied")
    private Set<StructuurVisieGebiedBeleidDto> beleid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuurVisieGebiedDto that = (StructuurVisieGebiedDto) o;
        return Objects.equals(planidentificatie, that.planidentificatie) && Objects.equals(identificatie, that.identificatie) && Objects.equals(naam, that.naam) && Objects.equals(thema, that.thema) && Objects.equals(beleid, that.beleid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planidentificatie, identificatie, naam, thema, beleid);
    }

    @Override
    public String toString() {
        return "StructuurVisieGebiedDto{" +
                "id=" + id +
                ", planidentificatie='" + planidentificatie + '\'' +
                ", identificatie='" + identificatie + '\'' +
                ", naam='" + naam + '\'' +
                ", thema=" + thema +
                ", beleid=" + beleid +
                '}';
    }
}
