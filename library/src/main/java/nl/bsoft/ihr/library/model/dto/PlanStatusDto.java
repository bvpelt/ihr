package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "planstatus", schema = "public", catalog = "ihr")
public class PlanStatusDto implements Serializable {
    private static final long serialVersionUID = 19L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "status")
    private String status;
    @Column(name = "datum")
    private LocalDate datum;

    @OneToMany(mappedBy = "planstatus")
    private Set<PlanDto> plannen = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanStatusDto that = (PlanStatusDto) o;
        return Objects.equals(status, that.status) && Objects.equals(datum, that.datum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, datum);
    }

    @Override
    public String toString() {
        return "PlanStatusDto{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
