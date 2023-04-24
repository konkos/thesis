package gr.uom.thesis.coverage;

import gr.uom.thesis.dependencies.TotalDependenciesComparator;
import gr.uom.thesis.project.entities.AnalyzedProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TotalCoverageComparatorRepository extends JpaRepository<TotalCoverageComparator, Long> {
    List<TotalCoverageComparator> findByAnalyzedProject1OrAnalyzedProject2(AnalyzedProject pr1, AnalyzedProject pr2);

    default List<TotalCoverageComparator> getAnalyzedProject(AnalyzedProject project) {
        return findByAnalyzedProject1OrAnalyzedProject2(project, project);
    }

}
