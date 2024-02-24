package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "plan", schema = "public", catalog = "ihr")
public class PlanDto {
    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "naam")
    private String naam;
    @Column(name = "besluitnummer")
    private String besluitNummer;
    @Column(name = "plantype")
    private String plantype;
    @Column(name = "planstatus")
    private String planstatus;
    @Column(name = "planstatusdate")
    private LocalDate planstatusdate;
    @Column(name = "regelstatus")
    private String regelstatus;
    @Column(name = "dossierid")
    private String dossierid;
    @Column(name = "dossierstatus")
    private String dossierstatus;

    // link to location -- do not use in equal check
    @Column(name = "md5hash", nullable = false)
    private String md5hash;

    public PlanDto(String identificatie, String naam, String plantype, String planstatus, LocalDate planstatusdate, String dossierid, String dossierstatus, String besluitNummer, String regelstatus, String md5hash) {
        this.identificatie = identificatie;
        this.naam = naam;
        this.besluitNummer = besluitNummer;
        this.plantype = plantype;
        this.planstatus = planstatus;
        this.planstatusdate = planstatusdate;
        this.regelstatus = regelstatus;
        this.dossierid = dossierid;
        this.dossierstatus = dossierstatus;
        this.md5hash = md5hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanDto planDto = (PlanDto) o;
        return Objects.equals(identificatie, planDto.identificatie) && Objects.equals(naam, planDto.naam) && Objects.equals(besluitNummer, planDto.besluitNummer) && Objects.equals(plantype, planDto.plantype) && Objects.equals(planstatus, planDto.planstatus) && Objects.equals(planstatusdate, planDto.planstatusdate) && Objects.equals(regelstatus, planDto.regelstatus) && Objects.equals(dossierid, planDto.dossierid) && Objects.equals(dossierstatus, planDto.dossierstatus) && Objects.equals(md5hash, planDto.md5hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, naam, besluitNummer, plantype, planstatus, planstatusdate, regelstatus, dossierid, dossierstatus, md5hash);
    }

    @Override
    public String toString() {
        return "PlanDto{" +
                "id=" + id +
                ", identificatie='" + identificatie + '\'' +
                ", naam='" + naam + '\'' +
                ", besluitNummer='" + besluitNummer + '\'' +
                ", plantype='" + plantype + '\'' +
                ", planstatus='" + planstatus + '\'' +
                ", planstatusdate=" + planstatusdate +
                ", regelstatus='" + regelstatus + '\'' +
                ", dossierid='" + dossierid + '\'' +
                ", dossierstatus='" + dossierstatus + '\'' +
                ", md5hash='" + md5hash + '\'' +
                '}';
    }
}
