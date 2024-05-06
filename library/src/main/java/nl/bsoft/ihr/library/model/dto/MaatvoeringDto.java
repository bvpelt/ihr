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
@Table(name = "maatvoering", schema = "public", catalog = "ihr")
public class MaatvoeringDto implements Serializable {
    private static final long serialVersionUID = 28L;

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
    @JoinTable(
            name = "maatvoering_omvang",
            joinColumns = @JoinColumn(name = "maatvoering_id"),
            inverseJoinColumns = @JoinColumn(name = "omvang_id"))
    private Set<OmvangDto> omvangen = new HashSet<>();
    @Column(name = "verwijzingnaartekst")
    private String verwijzingnaartekst;
    @Column(name = "styleid")
    private String styleid;
    @Column(name = "md5hash")
    private String md5hash;

    public void addOmvang(OmvangDto omvang) {
        this.omvangen.add(omvang);
        omvang.getMaatvoeringen().add(this);

    }

    public void removeOmvang(OmvangDto omvang) {
        this.omvangen.remove(omvang);
        omvang.getMaatvoeringen().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaatvoeringDto that = (MaatvoeringDto) o;
        return Objects.equals(planidentificatie, that.planidentificatie) && Objects.equals(identificatie, that.identificatie) && Objects.equals(naam, that.naam) && Objects.equals(verwijzingnaartekst, that.verwijzingnaartekst) && Objects.equals(styleid, that.styleid) && Objects.equals(md5hash, that.md5hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planidentificatie, identificatie, naam, verwijzingnaartekst, styleid, md5hash);
    }

    @Override
    public String toString() {
        return "MaatvoeringDto{" +
                "id=" + id +
                ", planidentificatie='" + planidentificatie + '\'' +
                ", identificatie='" + identificatie + '\'' +
                ", naam='" + naam + '\'' +
                ", verwijzingnaartekst='" + verwijzingnaartekst + '\'' +
                ", styleid='" + styleid + '\'' +
                ", md5hash='" + md5hash + '\'' +
                '}';
    }
}
