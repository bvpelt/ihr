package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "beleid", schema = "public", catalog = "ihr")
public class BeleidDto {
    private static final long serialVersionUID = 9L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "belang")
    private String belang;
    @Column(name = "rol")
    private String rol;
    @Column(name = "instrument")
    private String instrument;

    @ManyToMany(mappedBy = "beleid", fetch = FetchType.LAZY)
    private Set<StructuurVisieGebiedDto> structuurVisieGebied;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeleidDto beleidDto = (BeleidDto) o;
        return Objects.equals(belang, beleidDto.belang) && Objects.equals(rol, beleidDto.rol) && Objects.equals(instrument, beleidDto.instrument) && Objects.equals(structuurVisieGebied, beleidDto.structuurVisieGebied);
    }

    @Override
    public int hashCode() {
        return Objects.hash(belang, rol, instrument, structuurVisieGebied);
    }

    @Override
    public String toString() {
        return "BeleidDto{" +
                "id=" + id +
                ", belang='" + belang + '\'' +
                ", rol='" + rol + '\'' +
                ", instrument='" + instrument + '\'' +
                ", structuurVisieGebied=" + structuurVisieGebied +
                '}';
    }
}
