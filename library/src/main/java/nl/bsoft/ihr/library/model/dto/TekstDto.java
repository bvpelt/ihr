package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tekst", schema = "public", catalog = "ihr")
public class TekstDto {
    private static final long serialVersionUID = 6L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "planidentificatie")
    private String planidentificatie;
    @Column(name = "tekstidentificatie")
    private String tekstidentificatie;
    @Column(name = "titel")
    private String titel;
    @Column(name = "inhoud")
    private String inhoud;
    @Column(name = "volgnummer")
    private int volgNummer;
    @Column(name = "externhref")
    private String externHRef;
    @Column(name = "externlabel")
    private String externLabel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TekstDto tekstDto = (TekstDto) o;
        return volgNummer == tekstDto.volgNummer && Objects.equals(planidentificatie, tekstDto.planidentificatie) && Objects.equals(tekstidentificatie, tekstDto.tekstidentificatie) && Objects.equals(titel, tekstDto.titel) && Objects.equals(inhoud, tekstDto.inhoud) && Objects.equals(externHRef, tekstDto.externHRef) && Objects.equals(externLabel, tekstDto.externLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planidentificatie, tekstidentificatie, titel, inhoud, volgNummer, externHRef, externLabel);
    }

    @Override
    public String toString() {
        return "TekstDto{" +
                "id=" + id +
                ", planidentificatie='" + planidentificatie + '\'' +
                ", tekstidentificatie='" + tekstidentificatie + '\'' +
                ", titel='" + titel + '\'' +
                ", inhoud='" + inhoud + '\'' +
                ", volgNummer=" + volgNummer +
                ", externHRef='" + externHRef + '\'' +
                ", externLabel='" + externLabel + '\'' +
                '}';
    }
}
