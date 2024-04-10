package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plan", schema = "public", catalog = "ihr")
public class PlanDto {
    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "plantype")
    private String plantype;

    @ManyToOne
    @JoinColumn(name = "beleidsmatigeoverheid_id", nullable = false, referencedColumnName = "id")
    private OverheidDto beleidsmatigeoverheid;

    @ManyToOne
    @JoinColumn(name = "publicerendeoverheid_id", nullable = false, referencedColumnName = "id")
    private OverheidDto publicerendeoverheid;

    @Column(name = "naam")
    private String naam;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // owning site
    @JoinTable(
            name = "plan_locatienaam",
            joinColumns = {
                    @JoinColumn(name = "plan_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "locatienaam_id", referencedColumnName = "id")
            })
    private Set<LocatieNaamDto> locaties = new HashSet<>();

    @Column(name = "planstatus")
    private String planstatus;
    @Column(name = "planstatusdate")
    private LocalDate planstatusdate;
    @Column(name = "besluitnummer")
    private String besluitNummer;
    @Column(name = "regelstatus")
    private String regelstatus;
    @Column(name = "dossierid")
    private String dossierid;
    @Column(name = "dossierstatus")
    private String dossierstatus;

    @Column(name = "isparapluplan")
    private Boolean isParapluPlan;
    @Column(name = "beroepenbezwaar")
    private String beroepEnBezwaar;

    @Column(name = "md5hash", nullable = false)
    private String md5hash;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanDto planDto = (PlanDto) o;
        return Objects.equals(identificatie, planDto.identificatie) && Objects.equals(plantype, planDto.plantype) && Objects.equals(beleidsmatigeoverheid, planDto.beleidsmatigeoverheid) && Objects.equals(publicerendeoverheid, planDto.publicerendeoverheid) && Objects.equals(naam, planDto.naam) && Objects.equals(planstatus, planDto.planstatus) && Objects.equals(planstatusdate, planDto.planstatusdate) && Objects.equals(besluitNummer, planDto.besluitNummer) && Objects.equals(regelstatus, planDto.regelstatus) && Objects.equals(dossierid, planDto.dossierid) && Objects.equals(dossierstatus, planDto.dossierstatus) && Objects.equals(isParapluPlan, planDto.isParapluPlan) && Objects.equals(beroepEnBezwaar, planDto.beroepEnBezwaar) && Objects.equals(md5hash, planDto.md5hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, plantype, beleidsmatigeoverheid, publicerendeoverheid, naam, planstatus, planstatusdate, besluitNummer, regelstatus, dossierid, dossierstatus, isParapluPlan, beroepEnBezwaar, md5hash);
    }

    @Override
    public String toString() {
        return "PlanDto{" +
                "id=" + id +
                ", identificatie='" + identificatie + '\'' +
                ", plantype='" + plantype + '\'' +
                ", beleidsmatigeoverheid=" + beleidsmatigeoverheid +
                ", publicerendeoverheid=" + publicerendeoverheid +
                ", naam='" + naam + '\'' +
                ", planstatus='" + planstatus + '\'' +
                ", planstatusdate=" + planstatusdate +
                ", besluitNummer='" + besluitNummer + '\'' +
                ", regelstatus='" + regelstatus + '\'' +
                ", dossierid='" + dossierid + '\'' +
                ", dossierstatus='" + dossierstatus + '\'' +
                ", isParapluPlan=" + isParapluPlan +
                ", beroepEnBezwaar='" + beroepEnBezwaar + '\'' +
                ", md5hash='" + md5hash + '\'' +
                '}';
    }
}
