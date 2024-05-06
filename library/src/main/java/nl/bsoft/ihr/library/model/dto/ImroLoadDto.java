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
    @Column(name = "planloaded")
    private Boolean planloaded;
    @Column(name = "tekstenloaded")
    private Boolean tekstenLoaded;
    @Column(name = "tekstentried")
    private Boolean tekstentried;
    @Column(name = "bestemmingsvlakkenloaded")
    private Boolean bestemmingsvlakkenloaded;
    @Column(name = "bestemmingsvlakkentried")
    private Boolean bestemmingsvlakkentried;
    @Column(name = "structuurvisiegebiedloaded")
    private Boolean structuurvisiegebiedloaded;
    @Column(name = "structuurvisiegebiedtried")
    private Boolean structuurvisiegebiedtried;
    @Column(name = "bouwvlakkenloaded")
    private Boolean bouwvlakkenloaded;
    @Column(name = "bouwvlakkentried")
    private Boolean bouwvlakkentried;
    @Column(name = "functieaanduidingloaded")
    private Boolean functieaanduidingloaded;
    @Column(name = "functieaanduidingtried")
    private Boolean functieaanduidingtried;
    @Column(name = "bouwaanduidingloaded")
    private Boolean bouwaanduidingloaded;
    @Column(name = "bouwaanduidingtried")
    private Boolean bouwaanduidingtried;
    @Column(name = "lettertekenaanduidingloaded")
    private Boolean lettertekenaanduidingloaded;
    @Column(name = "lettertekenaanduidingtried")
    private Boolean lettertekenaanduidingtried;
    @Column(name = "maatvoeringloaded")
    private Boolean maatvoeringloaded;
    @Column(name = "maatvoeringtried")
    private Boolean maatvoeringtried;
    @Column(name = "figuurloaded")
    private Boolean figuurloaded;
    @Column(name = "figuurtried")
    private Boolean figuurtried;

    public ImroLoadDto(String identificatie, Boolean loaded) {
        this.identificatie = identificatie;
        this.loaded = loaded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImroLoadDto that = (ImroLoadDto) o;
        return Objects.equals(identificatie, that.identificatie) && Objects.equals(loaded, that.loaded) && Objects.equals(planloaded, that.planloaded) && Objects.equals(tekstenLoaded, that.tekstenLoaded) && Objects.equals(tekstentried, that.tekstentried) && Objects.equals(bestemmingsvlakkenloaded, that.bestemmingsvlakkenloaded) && Objects.equals(bestemmingsvlakkentried, that.bestemmingsvlakkentried) && Objects.equals(structuurvisiegebiedloaded, that.structuurvisiegebiedloaded) && Objects.equals(structuurvisiegebiedtried, that.structuurvisiegebiedtried) && Objects.equals(bouwvlakkenloaded, that.bouwvlakkenloaded) && Objects.equals(bouwvlakkentried, that.bouwvlakkentried) && Objects.equals(functieaanduidingloaded, that.functieaanduidingloaded) && Objects.equals(functieaanduidingtried, that.functieaanduidingtried) && Objects.equals(bouwaanduidingloaded, that.bouwaanduidingloaded) && Objects.equals(bouwaanduidingtried, that.bouwaanduidingtried) && Objects.equals(lettertekenaanduidingloaded, that.lettertekenaanduidingloaded) && Objects.equals(lettertekenaanduidingtried, that.lettertekenaanduidingtried) && Objects.equals(maatvoeringloaded, that.maatvoeringloaded) && Objects.equals(maatvoeringtried, that.maatvoeringtried) && Objects.equals(figuurloaded, that.figuurloaded) && Objects.equals(figuurtried, that.figuurtried);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, loaded, planloaded, tekstenLoaded, tekstentried, bestemmingsvlakkenloaded, bestemmingsvlakkentried, structuurvisiegebiedloaded, structuurvisiegebiedtried, bouwvlakkenloaded, bouwvlakkentried, functieaanduidingloaded, functieaanduidingtried, bouwaanduidingloaded, bouwaanduidingtried, lettertekenaanduidingloaded, lettertekenaanduidingtried, maatvoeringloaded, maatvoeringtried, figuurloaded, figuurtried);
    }

    @Override
    public String toString() {
        return "ImroLoadDto{" +
                "id=" + id +
                ", identificatie='" + identificatie + '\'' +
                ", loaded=" + loaded +
                ", planloaded=" + planloaded +
                ", tekstenLoaded=" + tekstenLoaded +
                ", tekstentried=" + tekstentried +
                ", bestemmingsvlakkenloaded=" + bestemmingsvlakkenloaded +
                ", bestemmingsvlakkentried=" + bestemmingsvlakkentried +
                ", structuurvisiegebiedloaded=" + structuurvisiegebiedloaded +
                ", structuurvisiegebiedtried=" + structuurvisiegebiedtried +
                ", bouwvlakkenloaded=" + bouwvlakkenloaded +
                ", bouwvlakkentried=" + bouwvlakkentried +
                ", functieaanduidingloaded=" + functieaanduidingloaded +
                ", functieaanduidingtried=" + functieaanduidingtried +
                ", bouwaanduidingloaded=" + bouwaanduidingloaded +
                ", bouwaanduidingtried=" + bouwaanduidingtried +
                ", lettertekenaanduidingloaded=" + lettertekenaanduidingloaded +
                ", lettertekenaanduidingtried=" + lettertekenaanduidingtried +
                ", maatvoeringloaded=" + maatvoeringloaded +
                ", maatvoeringtried=" + maatvoeringtried +
                ", figuurloaded=" + figuurloaded +
                ", figuurtried=" + figuurtried +
                '}';
    }
}
