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
@Table(name = "overheid", schema = "public", catalog = "ihr")
public class OverheidDto implements Serializable {
    private static final long serialVersionUID = 14L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type", length = 24)
    private String type;
    @Column(name = "code", length = 4)
    private String code;
    @Column(name = "naam")
    private String naam;
    @OneToMany(mappedBy = "beleidsmatigeoverheid")
    private Set<PlanDto> beleidsmatig = new HashSet<>();
    @OneToMany(mappedBy = "publicerendeoverheid")
    private Set<PlanDto> publicerend = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OverheidDto that = (OverheidDto) o;
        return Objects.equals(type, that.type) && Objects.equals(code, that.code) && Objects.equals(naam, that.naam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, code, naam);
    }

    @Override
    public String toString() {
        return "OverheidDto{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", naam='" + naam + '\'' +
                '}';
    }
}
