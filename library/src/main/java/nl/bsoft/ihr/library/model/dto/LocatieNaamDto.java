package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "locatienaam", schema = "public", catalog = "ihr")
public class LocatieNaamDto {
    private static final long serialVersionUID = 15L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "locatienaam")
    private String locatienaam;
    @ManyToMany(mappedBy = "locatienamen", fetch = FetchType.LAZY)
    private Set<PlanDto> plannen;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocatieNaamDto that = (LocatieNaamDto) o;
        return Objects.equals(locatienaam, that.locatienaam);
    }
    @Override
    public int hashCode() {
        return Objects.hash(locatienaam);
    }
    @Override
    public String toString() {
        return "LocatieNaamDto{" +
                "id=" + id +
                ", locatienaam='" + locatienaam + '\'' +
                ", plannen=" + plannen +
                '}';
    }
}
