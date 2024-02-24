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
@Table(name = "overheid", schema = "public", catalog = "ihr")
public class OverheidDto {
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    private String type;
    @Column(name = "code")
    private String code;
    @Column(name = "beleidsmatig")
    private Boolean beleidsmatig;
    @Column(name = "publicerend")
    private Boolean publicerend;
    @Column(name = "plan_identificatie")
    private String plan_identificatie;

    public OverheidDto(String type, String code, String naam, Boolean beleidsmatig, Boolean publicerend, String plan_identificatie) {
        this.type = type;
        this.code = code;
        this.beleidsmatig = beleidsmatig;
        this.publicerend = publicerend;
        this.plan_identificatie = plan_identificatie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OverheidDto that = (OverheidDto) o;
        return Objects.equals(type, that.type) && Objects.equals(code, that.code) && Objects.equals(beleidsmatig, that.beleidsmatig) && Objects.equals(publicerend, that.publicerend) && Objects.equals(plan_identificatie, that.plan_identificatie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, code, beleidsmatig, publicerend, plan_identificatie);
    }

    @Override
    public String toString() {
        return "OverheidDto{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", beleidsmatig=" + beleidsmatig +
                ", publicerend=" + publicerend +
                ", plan_identificatie='" + plan_identificatie + '\'' +
                '}';
    }
}
