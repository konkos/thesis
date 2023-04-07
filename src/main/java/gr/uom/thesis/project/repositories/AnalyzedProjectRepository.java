package gr.uom.thesis.project.repositories;

import gr.uom.thesis.project.entities.AnalyzedProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyzedProjectRepository extends JpaRepository<AnalyzedProject, Long> {
}
