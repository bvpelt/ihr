package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auditlog", schema = "public", catalog = "ihr")
public class AuditLogDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "planid")
    private String planid;

    @Column(name = "identificatie")
    private String identificatie;

    @Column(name = "objectsoort")
    private String objectsoort;

    @Column(name = "actie")
    private String actie;

    @Column(name = "registratie")
    private LocalDateTime registratie;

    public AuditLogDto(String planid, String objectsoort, String actie) {
        this.planid = planid;
        this.objectsoort = objectsoort;
        this.actie = actie;
        this.registratie = LocalDateTime.now();
    }

    public AuditLogDto(String planid, String identificatie, String objectsoort, String actie) {
        this.planid = planid;
        this.identificatie = identificatie;
        this.objectsoort = objectsoort;
        this.actie = actie;
        this.registratie = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditLogDto that = (AuditLogDto) o;
        return Objects.equals(planid, that.planid) && Objects.equals(identificatie, that.identificatie) && Objects.equals(objectsoort, that.objectsoort) && Objects.equals(actie, that.actie) && Objects.equals(registratie, that.registratie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planid, identificatie, objectsoort, actie, registratie);
    }

    @Override
    public String toString() {
        return "AuditLogDto{" +
                "id=" + id +
                ", planid='" + planid + '\'' +
                ", identificatie='" + identificatie + '\'' +
                ", objectsoort='" + objectsoort + '\'' +
                ", actie='" + actie + '\'' +
                ", registratie=" + registratie +
                '}';
    }
}
