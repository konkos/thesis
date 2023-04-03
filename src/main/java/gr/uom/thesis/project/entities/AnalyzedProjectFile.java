package gr.uom.thesis.project.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AnalyzedProjectFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    File firstFile;

    String name;

    int stmts;

    int miss;

    int coverage;

    @OneToMany
    @ToString.Exclude
    private List<Comment> comments;

    private Double rating;

    private Double previousRating;

    @ElementCollection
    private Map<String, Double> similarity;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private AnalyzedProject project;

    private String projectName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AnalyzedProjectFile that = (AnalyzedProjectFile) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
