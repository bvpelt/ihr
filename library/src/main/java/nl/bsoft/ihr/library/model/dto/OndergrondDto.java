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
@Table(name = "ondergrond", schema = "public", catalog = "ihr")
public class OndergrondDto {
    private static final long serialVersionUID = 22L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    private String type;
    @Column(name = "datum")
    private String datum;

    @ManyToMany(mappedBy = "ondergronden", fetch = FetchType.LAZY)
    private Set<PlanDto> plannen = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OndergrondDto that = (OndergrondDto) o;
        return Objects.equals(type, that.type) && Objects.equals(datum, that.datum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, datum);
    }

    @Override
    public String toString() {
        return "OndergrondDto{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", datum='" + datum + '\'' +
                '}';
    }
}
