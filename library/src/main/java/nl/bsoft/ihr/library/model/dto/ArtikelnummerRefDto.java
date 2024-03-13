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
@Table(name = "artikel", schema = "public", catalog = "ihr")
public class ArtikelnummerRefDto {
    private static final long serialVersionUID = 12L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "artikel")
    private String artikel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtikelnummerRefDto that = (ArtikelnummerRefDto) o;
        return Objects.equals(artikel, that.artikel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artikel);
    }

    @Override
    public String toString() {
        return "ArtikelnummerRefDto{" +
                "id=" + id +
                ", artikel='" + artikel + '\'' +
                '}';
    }
}
