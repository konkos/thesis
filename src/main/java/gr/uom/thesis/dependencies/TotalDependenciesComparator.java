package gr.uom.thesis.dependencies;

import gr.uom.thesis.project.entities.AnalyzedProject;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TotalDependenciesComparator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "analyzed_project_1_id", nullable = false)
    private AnalyzedProject analyzedProject1;

    @ManyToOne
    @JoinColumn(name = "analyzed_project_2_id")
    private AnalyzedProject analyzedProject2;

    private int totalDependenciesDifference;

}
