package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locatienaam", schema = "public", catalog = "ihr")
public class LocatieNaamDto {
    private static final long serialVersionUID = 15L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "naam")
    private String naam;
    @ManyToMany(mappedBy = "locaties", fetch = FetchType.LAZY)
    private Set<PlanDto> plannen = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocatieNaamDto that = (LocatieNaamDto) o;
        return Objects.equals(naam, that.naam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naam);
    }

    @Override
    public String toString() {
        return "LocatieNaamDto{" +
                "id=" + id +
                ", naam='" + naam + '\'' +
                '}';
    }
}
