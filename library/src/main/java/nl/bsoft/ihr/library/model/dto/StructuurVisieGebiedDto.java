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
@Table(name = "structuurvisiegebied", schema = "public", catalog = "ihr")
public class StructuurVisieGebiedDto {
    private static final long serialVersionUID = 8L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "planidentificatie")
    private String planidentificatie;
    @Column(name = "identificatie")
    private String identificatie;
    @Column(name = "naam")
    private String naam;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_thema",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "thema_id", referencedColumnName = "id")
            })
    private Set<ThemaDto> themas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_beleid",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "beleid_id", referencedColumnName = "id")
            })
    private Set<BeleidDto> beleid = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_tekstref",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tekstref_id", referencedColumnName = "id")
            })
    private Set<TekstRefDto> verwijzingNaarTekst;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_illustratie",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "illustratie_id", referencedColumnName = "id")
            })
    private Set<IllustratieDto> illustraties;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_externplan_tengevolgevan",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "externplan_id", referencedColumnName = "id")
            })
    private Set<ExternPlanDto> externeplan_tengevolgevan;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_externplan_gebruiktinformatieuit",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "externplan_id", referencedColumnName = "id")
            })
    private Set<ExternPlanDto> externeplan_gebruiktinformatieuit;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_externplan_uittewerkenin",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "externplan_id", referencedColumnName = "id")
            })
    private Set<ExternPlanDto> externeplan_uittewerkenin;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_externplan_uitgewerktin",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "externplan_id", referencedColumnName = "id")
            })
    private Set<ExternPlanDto> externeplan_uitgewerktin;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_cartografieinfo",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "cartografieinfo_id", referencedColumnName = "id")
            })
    private Set<CartografieInfoDto> cartografieinfo;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // owns the relation
    @JoinTable(name = "structuurvisiegebied_locatie",
            joinColumns = {
                    @JoinColumn(name = "structuurvisiegebied_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "locatie_id", referencedColumnName = "id")
            })
    private Set<LocatieDto> locaties = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuurVisieGebiedDto that = (StructuurVisieGebiedDto) o;
        return Objects.equals(planidentificatie, that.planidentificatie) && Objects.equals(identificatie, that.identificatie) && Objects.equals(naam, that.naam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planidentificatie, identificatie, naam);
    }

    @Override
    public String toString() {
        return "StructuurVisieGebiedDto{" +
                "id=" + id +
                ", planidentificatie='" + planidentificatie + '\'' +
                ", identificatie='" + identificatie + '\'' +
                ", naam='" + naam + '\'' +
                '}';
    }
}
