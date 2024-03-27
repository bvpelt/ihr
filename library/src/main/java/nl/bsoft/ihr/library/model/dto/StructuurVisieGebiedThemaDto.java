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
@Table(name = "structuurvisiegebiedthema", schema = "public", catalog = "ihr")
public class StructuurVisieGebiedThemaDto {
    private static final long serialVersionUID = 10L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "thema")
    private String thema;

    @ManyToOne
    @JoinColumn(name="structuurvisiegebied_id", nullable=false, referencedColumnName = "id")
    private StructuurVisieGebiedDto structuurVisieGebied;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuurVisieGebiedThemaDto that = (StructuurVisieGebiedThemaDto) o;
        return Objects.equals(thema, that.thema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thema);
    }

    @Override
    public String toString() {
        return "StructuurVisieGebiedThemaDto{" +
                "id=" + id +
                ", thema='" + thema + '\'' +
                '}';
    }
}
