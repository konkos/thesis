package gr.uom.thesis.project.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne
    private AnalyzedProjectFile analyzedProjectFile;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "analyzed_project_id", nullable = false)
    private AnalyzedProject analyzedProject;

}
