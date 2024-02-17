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
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "naam")
    private String naam;
    @Column(name = "plantype")
    private String plantype;
    @Column(name = "planstatus")
    private String planstatus;
    @Column(name = "planstatusdate")
    private LocalDate planstatusdate;

    public PlanDto(String identificatie, String naam, String plantype, String planstatus, LocalDate planstatusdate) {
        this.identificatie = identificatie;
        this.naam = naam;
        this.plantype = plantype;
        this.planstatus = planstatus;
        this.planstatusdate = planstatusdate;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanDto planDto = (PlanDto) o;
        return Objects.equals(identificatie, planDto.identificatie) && Objects.equals(naam, planDto.naam) && Objects.equals(plantype, planDto.plantype) && Objects.equals(planstatus, planDto.planstatus) && Objects.equals(planstatusdate, planDto.planstatusdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, naam, plantype, planstatus, planstatusdate);
    }
}
