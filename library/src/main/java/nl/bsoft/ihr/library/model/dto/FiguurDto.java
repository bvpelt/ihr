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
@Table(name = "figuur", schema = "public", catalog = "ihr")
public class FiguurDto {
    private static final long serialVersionUID = 30L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "planidentificatie")
    private String planidentificatie;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "naam")
    private String naam;
    @ManyToMany
    @JoinTable(
            name = "figuur_artikel",
            joinColumns = @JoinColumn(name = "figuur_id"),
            inverseJoinColumns = @JoinColumn(name = "artikel_id"))
    private Set<ArtikelDto> artikelnummers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "figuur_tekstref",
            joinColumns = {
                    @JoinColumn(name = "figuur_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tekstref_id", referencedColumnName = "id")
            })
    private Set<TekstRefDto> verwijzingnaartekst;
    @Column(name = "labelinfo")
    private String labelinfo;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "figuur_illustratie",
            joinColumns = {
                    @JoinColumn(name = "figuur_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "illustratie_id", referencedColumnName = "id")
            })
    private Set<IllustratieDto> illustraties;
    @Column(name = "styleid")
    private String styleid;
    @Column(name = "md5hash")
    private String md5hash;

    // Artikel
    public void addArtikel(ArtikelDto artikel) {
        this.artikelnummers.add(artikel);
        artikel.getFiguren().add(this);

    }

    public void removeArtikel(ArtikelDto artikel) {
        this.artikelnummers.remove(artikel);
        artikel.getFiguren().remove(this);
    }

    // tekst
    public void addVerwijzingNaarTekst(TekstRefDto tekst) {
        this.verwijzingnaartekst.add(tekst);
        tekst.getFiguren().add(this);

    }

    public void removeVerwijzingNaarTekst(TekstRefDto tekst) {
        this.verwijzingnaartekst.remove(tekst);
        tekst.getFiguren().remove(this);
    }

    // illustratie
    public void addIllustratie(IllustratieDto illustratie) {
        this.illustraties.add(illustratie);
        illustratie.getFiguren().add(this);

    }

    public void removeIllustratie(IllustratieDto illustratie) {
        this.illustraties.remove(illustratie);
        illustratie.getFiguren().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FiguurDto figuurDto = (FiguurDto) o;
        return Objects.equals(planidentificatie, figuurDto.planidentificatie) && Objects.equals(identificatie, figuurDto.identificatie) && Objects.equals(naam, figuurDto.naam) && Objects.equals(labelinfo, figuurDto.labelinfo) && Objects.equals(styleid, figuurDto.styleid) && Objects.equals(md5hash, figuurDto.md5hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planidentificatie, identificatie, naam, labelinfo, styleid, md5hash);
    }

    @Override
    public String toString() {
        return "FiguurDto{" +
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
