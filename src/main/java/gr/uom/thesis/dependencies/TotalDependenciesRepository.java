package gr.uom.thesis.dependencies;

import gr.uom.thesis.project.entities.AnalyzedProject;
import gr.uom.thesis.stmts.TotalStmtsComparator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TotalDependenciesRepository extends JpaRepository<TotalDependenciesComparator, Long> {

    List<TotalDependenciesComparator> findByAnalyzedProject1OrAnalyzedProject2(AnalyzedProject pr1, AnalyzedProject pr2);

    default List<TotalDependenciesComparator> getAnalyzedProject(AnalyzedProject project) {
        return findByAnalyzedProject1OrAnalyzedProject2(project, project);
    }
}
