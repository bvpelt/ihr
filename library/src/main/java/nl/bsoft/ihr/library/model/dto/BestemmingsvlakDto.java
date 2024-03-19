package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "bestemmingsvlak", schema = "public", catalog = "ihr")
public class BestemmingsvlakDto {
    private static final long serialVersionUID = 7L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "planidentificatie")
    private String planidentificatie;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "type")
    private String type;
    @Column(name = "naam")
    private String naam;
    @Column(name = "bestemmingshoofdgroep")
    private String bestemmingshoofdgroep;
    @Column(name = "labelinfo")
    private String labelInfo;
    @Column(name = "md5hash")
    private String md5hash;
    @Column(name = "artikelnummer")
    private String artikelnummer;

    @ManyToMany // owns the relation
    @JoinTable(
            name = "bestemmingsvlak_bestemmingsfunctie",
            joinColumns = @JoinColumn(name = "bestemmingsvlak_id"),
            inverseJoinColumns = @JoinColumn(name = "bestemmingsfunctie_id"))
    private Set<BestemmingFunctieDto> bestemmingsfuncties = new HashSet<BestemmingFunctieDto>();

    @ManyToMany // owns the relation
    @JoinTable(
            name = "bestemmingsvlak_tekstref",
            joinColumns = @JoinColumn(name = "bestemmingsvlak_id"),
            inverseJoinColumns = @JoinColumn(name = "tekstref_id"))
    private Set<TekstRefDto> verwijzingNaarTekst = new HashSet<TekstRefDto>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BestemmingsvlakDto that = (BestemmingsvlakDto) o;
        return Objects.equals(planidentificatie, that.planidentificatie) && Objects.equals(identificatie, that.identificatie) && Objects.equals(type, that.type) && Objects.equals(naam, that.naam) && Objects.equals(bestemmingshoofdgroep, that.bestemmingshoofdgroep) && Objects.equals(bestemmingsfuncties, that.bestemmingsfuncties) && Objects.equals(artikelnummer, that.artikelnummer) && Objects.equals(verwijzingNaarTekst, that.verwijzingNaarTekst) && Objects.equals(labelInfo, that.labelInfo) && Objects.equals(md5hash, that.md5hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planidentificatie, identificatie, type, naam, bestemmingshoofdgroep, bestemmingsfuncties, artikelnummer, verwijzingNaarTekst, labelInfo, md5hash);
    }
    @Override
    public String toString() {
        return "BestemmingsvlakDto{" +
                "id=" + id +
                ", planidentificatie='" + planidentificatie + '\'' +
                ", identificatie='" + identificatie + '\'' +
                ", type='" + type + '\'' +
                ", naam='" + naam + '\'' +
                ", bestemmingshoofdgroep='" + bestemmingshoofdgroep + '\'' +
                ", bestemmingsfuncties=" + bestemmingsfuncties +
                ", artikelnummer='" + artikelnummer + '\'' +
                ", verwijzingNaarTekst=" + verwijzingNaarTekst +
                ", labelInfo='" + labelInfo + '\'' +
                ", md5hash='" + md5hash + '\'' +
                '}';
    }
}
