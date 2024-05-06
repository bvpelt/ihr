package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "normadressant", schema = "public", catalog = "ihr")
public class NormadressantDto implements Serializable {
    private static final long serialVersionUID = 21L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "norm")
    private String norm;

    @ManyToMany(mappedBy = "normadressanten")
    private Set<PlanDto> plannen = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NormadressantDto that = (NormadressantDto) o;
        return Objects.equals(norm, that.norm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(norm);
    }

    @Override
    public String toString() {
        return "NormadressantDto{" +
                "id=" + id +
                ", norm='" + norm + '\'' +
                '}';
    }
}
