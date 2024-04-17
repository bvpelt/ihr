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
@Table(name = "kruimelpad", schema = "public", catalog = "ihr")
public class KruimelDto {
    private static final long serialVersionUID = 23L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "titel")
    private String titel;
    @Column(name = "volgnummer")
    private Integer volgnummer;

    @ManyToOne
    @JoinColumn(name = "tekst_id", nullable = false, referencedColumnName = "id")
    private TekstDto kruimelpad;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KruimelDto that = (KruimelDto) o;
        return Objects.equals(identificatie, that.identificatie) && Objects.equals(titel, that.titel) && Objects.equals(volgnummer, that.volgnummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, titel, volgnummer);
    }

    @Override
    public String toString() {
        return "KruimelDto{" +
                "id=" + id +
                ", identificatie='" + identificatie + '\'' +
                ", titel='" + titel + '\'' +
                ", volgnummer=" + volgnummer +'\'' +
                '}';
    }
}
