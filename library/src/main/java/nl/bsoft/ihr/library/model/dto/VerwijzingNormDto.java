package nl.bsoft.ihr.library.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "verwijzingnorm", schema = "public", catalog = "ihr")
public class VerwijzingNormDto {
    private static final long serialVersionUID = 20L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "norm")
    private String norm;

    @ManyToMany(mappedBy = "verwijzingnormen", fetch = FetchType.LAZY)
    private Set<PlanDto> plannen = new HashSet<>();
}
