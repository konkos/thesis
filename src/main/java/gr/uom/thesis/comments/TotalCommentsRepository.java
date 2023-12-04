package gr.uom.thesis.comments;

import gr.uom.thesis.project.entities.AnalyzedProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TotalCommentsRepository extends JpaRepository<TotalCommentsComparator, Long> {

    List<TotalCommentsComparator> findByAnalyzedProject1OrAnalyzedProject2(AnalyzedProject pr1, AnalyzedProject pr2);

    default List<TotalCommentsComparator> getAnalyzedProject(AnalyzedProject analyzedProject) {
        return findByAnalyzedProject1OrAnalyzedProject2(analyzedProject, analyzedProject);
    }


}
