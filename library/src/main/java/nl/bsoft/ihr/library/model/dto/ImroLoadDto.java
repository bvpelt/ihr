package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "imroload", schema = "public", catalog = "ihr")
public class ImroLoadDto {
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "loaded")
    private Boolean loaded;
    @Column(name = "tekstenloaded")
    private Boolean tekstenLoaded;
    @Column(name = "bestemmingsvlakkenloaded")
    private Boolean bestemmingsvlakkenloaded;
    @Column(name = "structuurvisiegebiedloaded")
    private Boolean structuurvisiegebiedloaded;
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
