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
@Table(name = "cartografieinfo", schema = "public", catalog = "ihr")
public class CartografieInfoDto {
    private static final long serialVersionUID = 18L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kaartnummer")
    private Integer kaartnummer;
    @Column(name = "kaartnaam")
    private String kaartnaam;
    @Column(name = "symboolcode")
    private String symboolcode;

    @ManyToMany(mappedBy = "cartografieinfo", fetch = FetchType.LAZY)
    private Set<StructuurVisieGebiedDto> structuurvisiegebied = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartografieInfoDto that = (CartografieInfoDto) o;
        return Objects.equals(kaartnummer, that.kaartnummer) && Objects.equals(kaartnaam, that.kaartnaam) && Objects.equals(symboolcode, that.symboolcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kaartnummer, kaartnaam, symboolcode);
    }

    @Override
    public String toString() {
        return "CartografieInfoDto{" +
                "id=" + id +
                ", kaartnummer='" + kaartnummer + '\'' +
                ", kaartnaam='" + kaartnaam + '\'' +
                ", symboolcode='" + symboolcode + '\'' +
                '}';
    }
}
