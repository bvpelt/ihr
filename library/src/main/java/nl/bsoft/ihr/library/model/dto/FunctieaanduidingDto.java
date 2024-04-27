package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "functieaanduiding", schema = "public", catalog = "ihr")
public class FunctieaanduidingDto {
    private static final long serialVersionUID = 25L;

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
    @Column(name = "verwijzingnaartekst")
    private String verwijzingnaartekst;
    @Column(name = "styleid")
    private String styleid;
    @Column(name = "md5hash")
    private String md5hash;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctieaanduidingDto that = (FunctieaanduidingDto) o;
        return Objects.equals(planidentificatie, that.planidentificatie) && Objects.equals(identificatie, that.identificatie) && Objects.equals(naam, that.naam) && Objects.equals(labelinfo, that.labelinfo) && Objects.equals(verwijzingnaartekst, that.verwijzingnaartekst) && Objects.equals(styleid, that.styleid) && Objects.equals(md5hash, that.md5hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planidentificatie, identificatie, naam, labelinfo, verwijzingnaartekst, styleid, md5hash);
    }

    @Override
    public String toString() {
        return "FunctieaanduidingDto{" +
                "id=" + id +
                ", planidentificatie='" + planidentificatie + '\'' +
                ", identificatie='" + identificatie + '\'' +
                ", naam='" + naam + '\'' +
                ", labelinfo='" + labelinfo + '\'' +
                ", verwijzingnaartekst='" + verwijzingnaartekst + '\'' +
                ", styleid='" + styleid + '\'' +
                ", md5hash='" + md5hash + '\'' +
                '}';
    }
}
