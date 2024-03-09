package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "structuurvisiegebiedbeleid", schema = "public", catalog = "ihr")
public class StructuurVisieGebiedBeleidDto {
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

    @ManyToOne
    @JoinColumn(name="structuurvisiegebied_id", nullable = false)
    private StructuurVisieGebiedDto structuurvisiegebied;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuurVisieGebiedBeleidDto that = (StructuurVisieGebiedBeleidDto) o;
        return Objects.equals(belang, that.belang) && Objects.equals(rol, that.rol) && Objects.equals(instrument, that.instrument) && Objects.equals(structuurvisiegebied, that.structuurvisiegebied);
    }

    @Override
    public int hashCode() {
        return Objects.hash(belang, rol, instrument, structuurvisiegebied);
    }

    @Override
    public String toString() {
        return "StructVisieGebiedBeleidDto{" +
                "id=" + id +
                ", belang='" + belang + '\'' +
                ", rol='" + rol + '\'' +
                ", instrument='" + instrument + '\'' +
                ", structuurvisiegebied=" + structuurvisiegebied +
                '}';
    }
}
