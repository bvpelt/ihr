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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_thema",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "thema_id", referencedColumnName = "id")
            })
    private Set<ThemaDto> themas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_beleid",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "beleid_id", referencedColumnName = "id")
            })
    private Set<BeleidDto> beleid = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_tekstref",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tekstref_id", referencedColumnName = "id")
            })
    private Set<TekstRefDto> verwijzingNaarTekst;

    @OneToMany(mappedBy = "structuurvisiegebied")
    private Set<IllustratieDto> illustraties;
    @OneToMany(mappedBy = "structuurvisiegebied")
    private Set<TekstRefDto> externeplannen;
    @OneToMany(mappedBy = "structuurvisiegebied")
    private Set<TekstRefDto> cartografieinfo;

    @Column(name = "md5hash", nullable = false)
    private String md5hash;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuurVisieGebiedDto that = (StructuurVisieGebiedDto) o;
        return Objects.equals(planidentificatie, that.planidentificatie) && Objects.equals(identificatie, that.identificatie) && Objects.equals(naam, that.naam) && Objects.equals(thema, that.thema) && Objects.equals(beleid, that.beleid) && Objects.equals(verwijzingNaarTekst, that.verwijzingNaarTekst) && Objects.equals(illustraties, that.illustraties) && Objects.equals(externeplannen, that.externeplannen) && Objects.equals(cartografieinfo, that.cartografieinfo) && Objects.equals(md5hash, that.md5hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planidentificatie, identificatie, naam, thema, beleid, verwijzingNaarTekst, illustraties, externeplannen, cartografieinfo, md5hash);
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
                ", verwijzingNaarTekst=" + verwijzingNaarTekst +
                ", illustraties=" + illustraties +
                ", externeplannen=" + externeplannen +
                ", cartografieinfo=" + cartografieinfo +
                ", md5hash='" + md5hash + '\'' +
                '}';
    }
}
