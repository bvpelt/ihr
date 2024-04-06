package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(name = "artikelnummer")
    private String artikelnummer;
    @Column(name = "labelinfo")
    private String labelInfo;
    @Column(name = "md5hash")
    private String md5hash;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "bestemmingsvlak_bestemmingsfunctie",
            joinColumns = {
            @JoinColumn(name = "bestemmingsvlak_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
            @JoinColumn(name = "bestemmingsfunctie_id", referencedColumnName = "id")
            })
    private Set<BestemmingFunctieDto> bestemmingsfuncties = new HashSet<BestemmingFunctieDto>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "bestemmingsvlak_tekstref",
            joinColumns = {
            @JoinColumn(name = "bestemmingsvlak_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
            @JoinColumn(name = "tekstref_id", referencedColumnName = "id")
            })
    private Set<TekstRefDto> verwijzingNaarTekst = new HashSet<TekstRefDto>();

    public void addBestemmingsfunctie(BestemmingFunctieDto bestemmingFunctie) {
        this.bestemmingsfuncties.add(bestemmingFunctie);
        bestemmingFunctie.getBestemmingsvlakken().add(this);

    }
    public void removeBestemmingsfunctie(BestemmingFunctieDto bestemmingFunctie) {
        this.bestemmingsfuncties.remove(bestemmingFunctie);
        bestemmingFunctie.getBestemmingsvlakken().remove(this);
    }

    public void addVerwijzingNaarTekst(TekstRefDto tekstRef) {
        this.verwijzingNaarTekst.add(tekstRef);
        tekstRef.getBestemmingsvlakken().add(this);
    }
    public void removeVerwijzingNaarTekst(TekstRefDto tekstRef) {
        this.verwijzingNaarTekst.remove(tekstRef);
        tekstRef.getBestemmingsvlakken().remove(this);
    }

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
