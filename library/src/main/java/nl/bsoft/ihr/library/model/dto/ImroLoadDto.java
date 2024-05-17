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
    @Column(name = "tekstenloadedprocesed")
    private Integer tekstenloadedprocesed;
    @Column(name = "tekstentried")
    private Boolean tekstentried;
    @Column(name = "bestemmingsvlakkenloaded")
    private Boolean bestemmingsvlakkenloaded;
    @Column(name = "bestemmingsvlakkenloadedprocesed")
    private Integer bestemmingsvlakkenloadedprocesed;
    @Column(name = "bestemmingsvlakkentried")
    private Boolean bestemmingsvlakkentried;
    @Column(name = "structuurvisiegebiedloaded")
    private Boolean structuurvisiegebiedloaded;
    @Column(name = "structuurvisiegebiedloadedprocesed")
    private Integer structuurvisiegebiedloadedprocesed;
    @Column(name = "structuurvisiegebiedtried")
    private Boolean structuurvisiegebiedtried;
    @Column(name = "bouwvlakkenloaded")
    private Boolean bouwvlakkenloaded;
    @Column(name = "bouwvlakkenloadedprocesed")
    private Integer bouwvlakkenloadedprocesed;
    @Column(name = "bouwvlakkentried")
    private Boolean bouwvlakkentried;
    @Column(name = "functieaanduidingloaded")
    private Boolean functieaanduidingloaded;
    @Column(name = "functieaanduidingloadedprocesed")
    private Integer functieaanduidingloadedprocesed;
    @Column(name = "functieaanduidingtried")
    private Boolean functieaanduidingtried;
    @Column(name = "bouwaanduidingloaded")
    private Boolean bouwaanduidingloaded;
    @Column(name = "bouwaanduidingloadedprocesed")
    private Integer bouwaanduidingloadedprocesed;
    @Column(name = "bouwaanduidingtried")
    private Boolean bouwaanduidingtried;
    @Column(name = "lettertekenaanduidingloaded")
    private Boolean lettertekenaanduidingloaded;
    @Column(name = "lettertekenaanduidingloadedprocesed")
    private Integer lettertekenaanduidingloadedprocesed;
    @Column(name = "lettertekenaanduidingtried")
    private Boolean lettertekenaanduidingtried;
    @Column(name = "maatvoeringloaded")
    private Boolean maatvoeringloaded;
    @Column(name = "maatvoeringloadedprocesed")
    private Integer maatvoeringloadedprocesed;
    @Column(name = "maatvoeringtried")
    private Boolean maatvoeringtried;
    @Column(name = "figuurloaded")
    private Boolean figuurloaded;
    @Column(name = "figuurloadedprocesed")
    private Integer figuurloadedprocesed;
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
        return Objects.equals(identificatie, that.identificatie) && Objects.equals(loaded, that.loaded) && Objects.equals(planloaded, that.planloaded) && Objects.equals(tekstenLoaded, that.tekstenLoaded) && Objects.equals(tekstenloadedprocesed, that.tekstenloadedprocesed) && Objects.equals(tekstentried, that.tekstentried) && Objects.equals(bestemmingsvlakkenloaded, that.bestemmingsvlakkenloaded) && Objects.equals(bestemmingsvlakkenloadedprocesed, that.bestemmingsvlakkenloadedprocesed) && Objects.equals(bestemmingsvlakkentried, that.bestemmingsvlakkentried) && Objects.equals(structuurvisiegebiedloaded, that.structuurvisiegebiedloaded) && Objects.equals(structuurvisiegebiedloadedprocesed, that.structuurvisiegebiedloadedprocesed) && Objects.equals(structuurvisiegebiedtried, that.structuurvisiegebiedtried) && Objects.equals(bouwvlakkenloaded, that.bouwvlakkenloaded) && Objects.equals(bouwvlakkenloadedprocesed, that.bouwvlakkenloadedprocesed) && Objects.equals(bouwvlakkentried, that.bouwvlakkentried) && Objects.equals(functieaanduidingloaded, that.functieaanduidingloaded) && Objects.equals(functieaanduidingloadedprocesed, that.functieaanduidingloadedprocesed) && Objects.equals(functieaanduidingtried, that.functieaanduidingtried) && Objects.equals(bouwaanduidingloaded, that.bouwaanduidingloaded) && Objects.equals(bouwaanduidingloadedprocesed, that.bouwaanduidingloadedprocesed) && Objects.equals(bouwaanduidingtried, that.bouwaanduidingtried) && Objects.equals(lettertekenaanduidingloaded, that.lettertekenaanduidingloaded) && Objects.equals(lettertekenaanduidingloadedprocesed, that.lettertekenaanduidingloadedprocesed) && Objects.equals(lettertekenaanduidingtried, that.lettertekenaanduidingtried) && Objects.equals(maatvoeringloaded, that.maatvoeringloaded) && Objects.equals(maatvoeringloadedprocesed, that.maatvoeringloadedprocesed) && Objects.equals(maatvoeringtried, that.maatvoeringtried) && Objects.equals(figuurloaded, that.figuurloaded) && Objects.equals(figuurloadedprocesed, that.figuurloadedprocesed) && Objects.equals(figuurtried, that.figuurtried);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, loaded, planloaded, tekstenLoaded, tekstenloadedprocesed, tekstentried, bestemmingsvlakkenloaded, bestemmingsvlakkenloadedprocesed, bestemmingsvlakkentried, structuurvisiegebiedloaded, structuurvisiegebiedloadedprocesed, structuurvisiegebiedtried, bouwvlakkenloaded, bouwvlakkenloadedprocesed, bouwvlakkentried, functieaanduidingloaded, functieaanduidingloadedprocesed, functieaanduidingtried, bouwaanduidingloaded, bouwaanduidingloadedprocesed, bouwaanduidingtried, lettertekenaanduidingloaded, lettertekenaanduidingloadedprocesed, lettertekenaanduidingtried, maatvoeringloaded, maatvoeringloadedprocesed, maatvoeringtried, figuurloaded, figuurloadedprocesed, figuurtried);
    }

    @Override
    public String toString() {
        return "ImroLoadDto{" +
                "id=" + id +
                ", identificatie='" + identificatie + '\'' +
                ", loaded=" + loaded +
                ", planloaded=" + planloaded +
                ", tekstenLoaded=" + tekstenLoaded +
                ", tekstenloadedprocesed=" + tekstenloadedprocesed +
                ", tekstentried=" + tekstentried +
                ", bestemmingsvlakkenloaded=" + bestemmingsvlakkenloaded +
                ", bestemmingsvlakkenloadedprocesed=" + bestemmingsvlakkenloadedprocesed +
                ", bestemmingsvlakkentried=" + bestemmingsvlakkentried +
                ", structuurvisiegebiedloaded=" + structuurvisiegebiedloaded +
                ", structuurvisiegebiedloadedprocesed=" + structuurvisiegebiedloadedprocesed +
                ", structuurvisiegebiedtried=" + structuurvisiegebiedtried +
                ", bouwvlakkenloaded=" + bouwvlakkenloaded +
                ", bouwvlakkenloadedprocesed=" + bouwvlakkenloadedprocesed +
                ", bouwvlakkentried=" + bouwvlakkentried +
                ", functieaanduidingloaded=" + functieaanduidingloaded +
                ", functieaanduidingloadedprocesed=" + functieaanduidingloadedprocesed +
                ", functieaanduidingtried=" + functieaanduidingtried +
                ", bouwaanduidingloaded=" + bouwaanduidingloaded +
                ", bouwaanduidingloadedprocesed=" + bouwaanduidingloadedprocesed +
                ", bouwaanduidingtried=" + bouwaanduidingtried +
                ", lettertekenaanduidingloaded=" + lettertekenaanduidingloaded +
                ", lettertekenaanduidingloadedprocesed=" + lettertekenaanduidingloadedprocesed +
                ", lettertekenaanduidingtried=" + lettertekenaanduidingtried +
                ", maatvoeringloaded=" + maatvoeringloaded +
                ", maatvoeringloadedprocesed=" + maatvoeringloadedprocesed +
                ", maatvoeringtried=" + maatvoeringtried +
                ", figuurloaded=" + figuurloaded +
                ", figuurloadedprocesed=" + figuurloadedprocesed +
                ", figuurtried=" + figuurtried +
                '}';
    }
}
