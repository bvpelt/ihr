package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "externplan", schema = "public", catalog = "ihr")
public class ExternPlanDto {
    private static final long serialVersionUID = 17L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "naam")
    private String naam;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "planstatus")
    private String planstatus;
    @Column(name = "planstatusdate")
    private LocalDate planstatusdate;
    @Column(name = "dossier")
    private String dossier;
    @Column(name = "href")
    private String href;

    // relatiesMetExternePlannen
    @ManyToOne
    @JoinColumn(name = "vervangtmetplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto vervangtmetplan;

    @ManyToOne
    @JoinColumn(name = "tengevolgevanmetplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto tengevolgevanmetplan;

    @ManyToOne
    @JoinColumn(name = "muteertmetplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto muteertmetplan;

    @ManyToOne
    @JoinColumn(name = "gebruiktinfouitmetplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto gebruiktinfouitmetplan;

    @ManyToOne
    @JoinColumn(name = "gedeeltelijkeherzieningmetplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto gedeeltelijkeherzieningmetplan;

    @ManyToOne
    @JoinColumn(name = "uittewerkinginmetplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto uittewerkinginmetplan;

    @ManyToOne
    @JoinColumn(name = "uitgewerktinmetplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto uitgewerktinmetplan;

    // relatiesVanuitExternePlannen
    @ManyToOne
    @JoinColumn(name = "vervangtvanuitplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto vervangtvanuitplan;

    @ManyToOne
    @JoinColumn(name = "tegevolgevanvanuitplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto tegevolgevanvanuitplan;

    @ManyToOne
    @JoinColumn(name = "muteertvanuitplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto muteertvanuitplan;

    @ManyToOne
    @JoinColumn(name = "gebruiktinforuitvanuitplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto gebruiktinforuitvanuitplan;

    @ManyToOne
    @JoinColumn(name = "gedeeltelijkeherzieningvanuitplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto gedeeltelijkeherzieningvanuitplan;

    @ManyToOne
    @JoinColumn(name = "uittewerkinginvanuitplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto uittewerkinginvanuitplan;

    @ManyToOne
    @JoinColumn(name = "uitgewerktinvanuitplan_id", nullable = false, referencedColumnName = "id")
    private PlanDto uitgewerktinvanuitplan;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternPlanDto that = (ExternPlanDto) o;
        return Objects.equals(naam, that.naam) && Objects.equals(identificatie, that.identificatie) && Objects.equals(planstatus, that.planstatus) && Objects.equals(planstatusdate, that.planstatusdate) && Objects.equals(href, that.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naam, identificatie, planstatus, planstatusdate, href);
    }

    @Override
    public String toString() {
        return "ExternPlanDto{" +
                "id=" + id +
                ", naam='" + naam + '\'' +
                ", identificatie='" + identificatie + '\'' +
                ", planstatus='" + planstatus + '\'' +
                ", planstatusdate=" + planstatusdate +
                ", href='" + href + '\'' +
                '}';
    }
}
