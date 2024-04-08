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
