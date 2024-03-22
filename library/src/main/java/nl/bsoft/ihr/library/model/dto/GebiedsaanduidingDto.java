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
@Table(name = "gebiedsaanduiding", schema = "public", catalog = "ihr")
public class GebiedsaanduidingDto {
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "naam")
    private String naam;
    @Column(name = "gebiedsaanduidinggroep")
    private String gebiedsaanduidinggroep;
    @Column(name = "labelinfo")
    private String labelinfo;
    @Column(name = "md5hash", nullable = false)
    private String md5hash;

    @ManyToMany // owns relation
    @JoinTable(
            name = "gebiedsaanwijzing_artikel",
            joinColumns = @JoinColumn(name = "gebiedsaanwijzing_id"),
            inverseJoinColumns = @JoinColumn(name = "artikel_id"))
    private Set<ArtikelnummerRefDto> artikelnummers = new HashSet<>();

    @ManyToMany // owns relation
    @JoinTable(
            name = "gebiedsaanwijzing_tekstref",
            joinColumns = @JoinColumn(name = "gebiedsaanwijzing_id"),
            inverseJoinColumns = @JoinColumn(name = "tekstref_id"))
    private Set<TekstRefDto> verwijzingNaarTekst = new HashSet<>();

    @ManyToMany // owns relation
    @JoinTable(
            name = "gebiedsaanwijzing_bestemmingsfunctie",
            joinColumns = @JoinColumn(name = "gebiedsaanwijzing_id"),
            inverseJoinColumns = @JoinColumn(name = "bestemmingsfunctie_id"))
    private Set<BestemmingFunctieDto> bestemmingfuncties = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GebiedsaanduidingDto that = (GebiedsaanduidingDto) o;
        return Objects.equals(identificatie, that.identificatie) && Objects.equals(naam, that.naam) && Objects.equals(gebiedsaanduidinggroep, that.gebiedsaanduidinggroep) && Objects.equals(labelinfo, that.labelinfo) && Objects.equals(md5hash, that.md5hash) && Objects.equals(artikelnummers, that.artikelnummers) && Objects.equals(verwijzingNaarTekst, that.verwijzingNaarTekst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, naam, gebiedsaanduidinggroep, labelinfo, md5hash, artikelnummers, verwijzingNaarTekst);
    }

    @Override
    public String toString() {
        return "GebiedsaanduidingDto{" +
                "id=" + id +
                ", identificatie='" + identificatie + '\'' +
                ", naam='" + naam + '\'' +
                ", gebiedsaanduidinggroep='" + gebiedsaanduidinggroep + '\'' +
                ", labelinfo='" + labelinfo + '\'' +
                ", md5hash='" + md5hash + '\'' +
                ", artikelnummers=" + artikelnummers +
                ", verwijzingNaarTekst=" + verwijzingNaarTekst +
                ", bestermmingsfuncties=" + bestemmingfuncties +
                '}';
    }
}
