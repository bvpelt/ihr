package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lettertekenaanduiding", schema = "public", catalog = "ihr")
public class LettertekenaanduidingDto implements Serializable {
    private static final long serialVersionUID = 27L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "planidentificatie")
    private String planidentificatie;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "naam")
    private String naam;
    @Column(name = "labelinfo")
    private String labelinfo;
    @Column(name = "styleid")
    private String styleid;
    @Column(name = "md5hash")
    private String md5hash;

    @ManyToMany // owns relation
    @JoinTable(
            name = "lettertekenaanduiding_bestemmingsfunctie",
            joinColumns = @JoinColumn(name = "lettertekenaanduiding_id"),
            inverseJoinColumns = @JoinColumn(name = "bestemmingsfunctie_id"))
    private Set<BestemmingFunctieDto> bestemmingfuncties = new HashSet<>();

    public void addBestemmingsfunctie(BestemmingFunctieDto bestemmingFunctie) {
        this.bestemmingfuncties.add(bestemmingFunctie);
        bestemmingFunctie.getLettertekenaanduidingen().add(this);

    }

    public void removeBestemmingsfunctie(BestemmingFunctieDto bestemmingFunctie) {
        this.bestemmingfuncties.remove(bestemmingFunctie);
        bestemmingFunctie.getLettertekenaanduidingen().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LettertekenaanduidingDto that = (LettertekenaanduidingDto) o;
        return Objects.equals(planidentificatie, that.planidentificatie) && Objects.equals(identificatie, that.identificatie) && Objects.equals(naam, that.naam) && Objects.equals(labelinfo, that.labelinfo) && Objects.equals(styleid, that.styleid) && Objects.equals(md5hash, that.md5hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planidentificatie, identificatie, naam, labelinfo, styleid, md5hash);
    }

    @Override
    public String toString() {
        return "LettertekenaanduidingDto{" +
                "id=" + id +
                ", planidentificatie='" + planidentificatie + '\'' +
                ", identificatie='" + identificatie + '\'' +
                ", naam='" + naam + '\'' +
                ", labelinfo='" + labelinfo + '\'' +
                ", styleid='" + styleid + '\'' +
                ", md5hash='" + md5hash + '\'' +
                '}';
    }
}
