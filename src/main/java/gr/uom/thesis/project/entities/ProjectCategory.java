package gr.uom.thesis.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectCategory {
    //    {"entity": "I-PER", "score": 0.99861741065979, "index": 4, "word": "sarah", "start": 11, "end": 16}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    private String entity;

    @Column(unique = true)
    private String word;

    @ManyToMany(mappedBy = "categories")
    private List<AnalyzedProject> projects;

}
