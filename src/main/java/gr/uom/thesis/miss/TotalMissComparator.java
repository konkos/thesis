package gr.uom.thesis.miss;

import gr.uom.thesis.project.entities.AnalyzedProject;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "analyzedProject1", "analyzedProject2" }) })
public class TotalMissComparator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "analyzed_project_1_id", nullable = false)
    private AnalyzedProject analyzedProject1;

    @ManyToOne
    @JoinColumn(name = "analyzed_project_2_id")
    private AnalyzedProject analyzedProject2;

    private int totalMissDifference;
}
