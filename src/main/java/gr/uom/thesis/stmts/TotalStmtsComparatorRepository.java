package gr.uom.thesis.stmts;

import gr.uom.thesis.project.entities.AnalyzedProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TotalStmtsComparatorRepository extends JpaRepository<TotalStmtsComparator, Long> {


    List<TotalStmtsComparator> findByAnalyzedProject1OrAnalyzedProject2(AnalyzedProject pr1, AnalyzedProject pr2);

    default List<TotalStmtsComparator> getAnalyzedProject(AnalyzedProject project) {
        return findByAnalyzedProject1OrAnalyzedProject2(project, project);
    }
}
