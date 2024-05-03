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
@Table(name = "omvang", schema = "public", catalog = "ihr")
public class OmvangDto {
    private static final long serialVersionUID = 29L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naam")
    private String naam;
    @Column(name = "waarde")
    private String waarde;

    @ManyToMany(mappedBy = "omvangen", fetch = FetchType.LAZY)
    private Set<MaatvoeringDto> maatvoeringen = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OmvangDto omvangDto = (OmvangDto) o;
        return Objects.equals(naam, omvangDto.naam) && Objects.equals(waarde, omvangDto.waarde);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naam, waarde);
    }

    @Override
    public String toString() {
        return "OmvangDto{" +
                "id=" + id +
                ", naam='" + naam + '\'' +
                ", waarde='" + waarde + '\'' +
                '}';
    }
}
