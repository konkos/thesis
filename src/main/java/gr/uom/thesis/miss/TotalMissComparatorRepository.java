package gr.uom.thesis.miss;

import gr.uom.thesis.dependencies.TotalDependenciesComparator;
import gr.uom.thesis.project.entities.AnalyzedProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TotalMissComparatorRepository extends JpaRepository<TotalMissComparator, Long> {

    List<TotalMissComparator> findByAnalyzedProject1OrAnalyzedProject2(AnalyzedProject pr1, AnalyzedProject pr2);

    default List<TotalMissComparator> getAnalyzedProject(AnalyzedProject project) {
        return findByAnalyzedProject1OrAnalyzedProject2(project, project);
    }
}
