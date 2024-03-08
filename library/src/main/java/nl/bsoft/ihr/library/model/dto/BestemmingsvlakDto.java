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
    // private String [] bestemmingsfuncties;
    @Column(name = "artikelnummer")
    private String artikelnummer;
    //private String [] verwijzingNaarTekst;
    @Column(name = "labelinfo")
    private String labelInfo;
    @Column(name = "md5hash")
    private String md5hash;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BestemmingsvlakDto that = (BestemmingsvlakDto) o;
        return Objects.equals(identificatie, that.identificatie) && Objects.equals(type, that.type) && Objects.equals(naam, that.naam) && Objects.equals(bestemmingshoofdgroep, that.bestemmingshoofdgroep) && Objects.equals(artikelnummer, that.artikelnummer) && Objects.equals(labelInfo, that.labelInfo) && Objects.equals(md5hash, that.md5hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, type, naam, bestemmingshoofdgroep, artikelnummer, labelInfo, md5hash);
    }

    @Override
    public String toString() {
        return "BestemmingsvlakDto{" +
                "id=" + id +
                ", identificatie='" + identificatie + '\'' +
                ", type='" + type + '\'' +
                ", naam='" + naam + '\'' +
                ", bestemmingshoofdgroep='" + bestemmingshoofdgroep + '\'' +
                ", artikelnummer='" + artikelnummer + '\'' +
                ", labelInfo='" + labelInfo + '\'' +
                ", md5hash='" + md5hash + '\'' +
                '}';
    }
}
