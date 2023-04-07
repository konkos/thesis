package gr.uom.thesis.project.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class AnalyzedProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String gitUrl;

    private String owner;

    private String name;

    private String directory;

    @ElementCollection
    private ArrayList<String> dependencies;

    private int dependenciesCounter;

/*    @OneToMany(mappedBy = "project")
    @ToString.Exclude
    private List<AnalyzedProjectFile> files;*/

    private int totalCoverage;

    private int totalMiss;

    private int totalStmts;

    @Column(unique = true)
    private String sha;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AnalyzedProject that = (AnalyzedProject) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
