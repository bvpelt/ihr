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
@Table(name = "thema", schema = "public", catalog = "ihr")
public class ThemaDto {
    private static final long serialVersionUID = 10L;
    @ManyToMany(mappedBy = "themas", fetch = FetchType.LAZY)
    Set<StructuurVisieGebiedDto> structuurVisieGebieden;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "thema")
    private String thema;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThemaDto themaDto = (ThemaDto) o;
        return Objects.equals(thema, themaDto.thema) && Objects.equals(structuurVisieGebieden, themaDto.structuurVisieGebieden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thema, structuurVisieGebieden);
    }

    @Override
    public String toString() {
        return "ThemaDto{" +
                "id=" + id +
                ", thema='" + thema + '\'' +
                ", structuurVisieGebieden=" + structuurVisieGebieden +
                '}';
    }
}
