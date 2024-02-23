package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "imroload", schema = "public", catalog = "ihr")
public class ImroLoadDto {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "loaded")
    private Boolean loaded;

    public ImroLoadDto(String identificatie, Boolean loaded) {
        this.identificatie = identificatie;
        this.loaded = loaded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImroLoadDto that = (ImroLoadDto) o;
        return Objects.equals(identificatie, that.identificatie) && Objects.equals(loaded, that.loaded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, loaded);
    }

    @Override
    public String toString() {
        return "ImroLoadDto{" +
                "id=" + id +
                ", identificatie='" + identificatie + '\'' +
                ", loaded=" + loaded +
                '}';
    }
}
