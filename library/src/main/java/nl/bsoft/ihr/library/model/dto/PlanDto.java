package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CollectionId;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @ManyToOne
    @JoinColumn(name = "planstatus_id", nullable = false, referencedColumnName = "id")
    private PlanStatusDto planstatus;
    @Column(name = "verwijzingnaarvaststelling")
    private String verwijzingnaarvaststelling;
    @Column(name = "verwijzingnaargml")
    private String verwijzingnaargml;
    @Column(name = "besluitnummer")
    private String besluitnummer;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // owning site
    @JoinTable(
            name = "plan_verwijzingnorm",
            joinColumns = {
                    @JoinColumn(name = "plan_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "verwijzingnorm_id", referencedColumnName = "id")
            })
    private Set<VerwijzingNormDto> verwijzingnormen = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // owning site
    @JoinTable(
            name = "plan_normadressant",
            joinColumns = {
                    @JoinColumn(name = "plan_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "normadressant_id", referencedColumnName = "id")
            })
    private Set<NormadressantDto> normadressanten = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // owning site
    @JoinTable(
            name = "plan_ondergrond",
            joinColumns = {
                    @JoinColumn(name = "plan_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ondergrond_id", referencedColumnName = "id")
            })
    private Set<OndergrondDto> ondergronden = new HashSet<>();
    // relatiesMetExternePlannen
    @OneToMany(mappedBy = "vervangtmetplan")
    Set<ExternPlanDto> vervangtMetPlannen = new HashSet<>();
    @OneToMany(mappedBy = "tengevolgevanmetplan")
    Set<ExternPlanDto> tengevolgeVanMetPlannen = new HashSet<>();
    @OneToMany(mappedBy = "muteertmetplan")
    Set<ExternPlanDto> muteertMetPlannen = new HashSet<>();
    @OneToMany(mappedBy = "gebruiktinfouitmetplan")
    Set<ExternPlanDto> gebruiktInfoUitMetPlannen = new HashSet<>();
    @OneToMany(mappedBy = "gedeeltelijkeherzieningmetplan")
    Set<ExternPlanDto> gedeeltelijkeHerzieningMetPlannen = new HashSet<>();
    @OneToMany(mappedBy = "uittewerkinginmetplan")
    Set<ExternPlanDto> uitTeWerkenInMetPlannen = new HashSet<>();
    @OneToMany(mappedBy = "uitgewerktinmetplan")
    Set<ExternPlanDto> uitgewerktInMetPlannen = new HashSet<>();
    // relatiesVanuitExternePlannen
    @OneToMany(mappedBy = "vervangtvanuitplan")
    Set<ExternPlanDto> vervangtVanuitPlannen = new HashSet<>();
    @OneToMany(mappedBy = "tegevolgevanvanuitplan")
    Set<ExternPlanDto> tengevolgeVanVanuitPlannen = new HashSet<>();
    @OneToMany(mappedBy = "muteertvanuitplan")
    Set<ExternPlanDto> muteertVanuitPlannen = new HashSet<>();
    @OneToMany(mappedBy = "gebruiktinforuitvanuitplan")
    Set<ExternPlanDto> gebruiktInfoUitVanuitPlannen = new HashSet<>();
    @OneToMany(mappedBy = "gedeeltelijkeherzieningvanuitplan")
    Set<ExternPlanDto> gedeeltelijkeHerzieningVanuitPlannen = new HashSet<>();
    @OneToMany(mappedBy = "uittewerkinginvanuitplan")
    Set<ExternPlanDto> uitTeWerkenInVanuitPlannen = new HashSet<>();
    @OneToMany(mappedBy = "uitgewerktinvanuitplan")
    Set<ExternPlanDto> uitgewerktInVanuitPlannen = new HashSet<>();
    @OneToMany(mappedBy = "plan")
    Set<IllustratieDto> illustraties = new HashSet<>();
    @Column(name = "regelstatus")
    private String regelstatus;
    @Column(name = "dossierid")
    private String dossierid;
    @Column(name = "dossierstatus")
    private String dossierstatus;
    @Column(name = "ishistorisch")
    private Boolean ishistorisch;
    @Column(name = "verwijderdop")
    private LocalDateTime verwijderdop;
    @Column(name = "istamplan")
    private Boolean istamplan;
    @Column(name = "einderechtsgeldigheid")
    private LocalDate einderechtsgeldigheid;
    @Column(name = "isparapluplan")
    private Boolean isparapluplan;
    @Column(name = "beroepenbezwaar")
    private String beroepenbezwaar;

    @Column(name = "md5hash", nullable = false)
    private String md5hash;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanDto planDto = (PlanDto) o;
        return Objects.equals(identificatie, planDto.identificatie) && Objects.equals(plantype, planDto.plantype) && Objects.equals(beleidsmatigeoverheid, planDto.beleidsmatigeoverheid) && Objects.equals(publicerendeoverheid, planDto.publicerendeoverheid) && Objects.equals(naam, planDto.naam) && Objects.equals(planstatus, planDto.planstatus) && Objects.equals(verwijzingnaarvaststelling, planDto.verwijzingnaarvaststelling) && Objects.equals(verwijzingnaargml, planDto.verwijzingnaargml) && Objects.equals(besluitnummer, planDto.besluitnummer) && Objects.equals(regelstatus, planDto.regelstatus) && Objects.equals(dossierid, planDto.dossierid) && Objects.equals(dossierstatus, planDto.dossierstatus) && Objects.equals(ishistorisch, planDto.ishistorisch) && Objects.equals(verwijderdop, planDto.verwijderdop) && Objects.equals(istamplan, planDto.istamplan) && Objects.equals(einderechtsgeldigheid, planDto.einderechtsgeldigheid) && Objects.equals(isparapluplan, planDto.isparapluplan) && Objects.equals(beroepenbezwaar, planDto.beroepenbezwaar) && Objects.equals(md5hash, planDto.md5hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, plantype, beleidsmatigeoverheid, publicerendeoverheid, naam, planstatus, verwijzingnaarvaststelling, verwijzingnaargml, besluitnummer, regelstatus, dossierid, dossierstatus, ishistorisch, verwijderdop, istamplan, einderechtsgeldigheid, isparapluplan, beroepenbezwaar, md5hash);
    }

    @Override
    public String toString() {
        return "PlanDto{" +
                "identificatie='" + identificatie + '\'' +
                ", plantype='" + plantype + '\'' +
                ", beleidsmatigeoverheid=" + beleidsmatigeoverheid +
                ", publicerendeoverheid=" + publicerendeoverheid +
                ", naam='" + naam + '\'' +
                ", planstatus=" + planstatus +
                ", verwijzingnaarvaststelling='" + verwijzingnaarvaststelling + '\'' +
                ", verwijzingnaargml='" + verwijzingnaargml + '\'' +
                ", besluitnummer='" + besluitnummer + '\'' +
                ", regelstatus='" + regelstatus + '\'' +
                ", dossierid='" + dossierid + '\'' +
                ", dossierstatus='" + dossierstatus + '\'' +
                ", ishistorisch=" + ishistorisch +
                ", verwijderdop=" + verwijderdop +
                ", istamplan=" + istamplan +
                ", einderechtsgeldigheid=" + einderechtsgeldigheid +
                ", isparapluplan=" + isparapluplan +
                ", beroepenbezwaar='" + beroepenbezwaar + '\'' +
                ", md5hash='" + md5hash + '\'' +
                '}';
    }
}
