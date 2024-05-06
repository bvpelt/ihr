package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "imroload", schema = "public", catalog = "ihr")
public class ImroLoadDto implements Serializable {
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "loaded")
    private Boolean loaded;
    @Column(name = "tekstenloaded")
    private Boolean tekstenLoaded;
    @Column(name = "bestemmingsvlakkenloaded")
    private Boolean bestemmingsvlakkenloaded;
    @Column(name = "bouwvlakkenloaded")
    private Boolean bouwvlakkenloaded;
    @Column(name = "functieaanduidingloaded")
    private Boolean functieaanduidingloaded;
    @Column(name = "bouwaanduidingloaded")
    private Boolean bouwaanduidingloaded;
    @Column(name = "lettertekenaanduidingloaded")
    private Boolean lettertekenaanduidingloaded;
    @Column(name = "maatvoeringloaded")
    private Boolean maatvoeringloaded;
    @Column(name = "figuurloaded")
    private Boolean figuurloaded;
    @Column(name = "structuurvisiegebiedloaded")
    private Boolean structuurvisiegebiedloaded;

    public ImroLoadDto(String identificatie, Boolean loaded) {
        this.identificatie = identificatie;
        this.loaded = loaded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImroLoadDto that = (ImroLoadDto) o;
        return Objects.equals(identificatie, that.identificatie) && Objects.equals(loaded, that.loaded) && Objects.equals(tekstenLoaded, that.tekstenLoaded) && Objects.equals(bestemmingsvlakkenloaded, that.bestemmingsvlakkenloaded) && Objects.equals(bouwvlakkenloaded, that.bouwvlakkenloaded) && Objects.equals(functieaanduidingloaded, that.functieaanduidingloaded) && Objects.equals(bouwaanduidingloaded, that.bouwaanduidingloaded) && Objects.equals(lettertekenaanduidingloaded, that.lettertekenaanduidingloaded) && Objects.equals(maatvoeringloaded, that.maatvoeringloaded) && Objects.equals(figuurloaded, that.figuurloaded) && Objects.equals(structuurvisiegebiedloaded, that.structuurvisiegebiedloaded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, loaded, tekstenLoaded, bestemmingsvlakkenloaded, bouwvlakkenloaded, functieaanduidingloaded, bouwaanduidingloaded, lettertekenaanduidingloaded, maatvoeringloaded, figuurloaded, structuurvisiegebiedloaded);
    }

    @Override
    public String toString() {
        return "ImroLoadDto{" +
                "id=" + id +
                ", identificatie='" + identificatie + '\'' +
                ", loaded=" + loaded +
                ", tekstenLoaded=" + tekstenLoaded +
                ", bestemmingsvlakkenloaded=" + bestemmingsvlakkenloaded +
                ", bouwvlakkenloaded=" + bouwvlakkenloaded +
                ", functieaanduidingloaded=" + functieaanduidingloaded +
                ", bouwaanduidingloaded=" + bouwaanduidingloaded +
                ", lettertekenaanduidingloaded=" + lettertekenaanduidingloaded +
                ", maatvoeringloaded=" + maatvoeringloaded +
                ", figuurloaded=" + figuurloaded +
                ", structuurvisiegebiedloaded=" + structuurvisiegebiedloaded +
                '}';
    }
}
