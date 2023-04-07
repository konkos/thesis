package gr.uom.thesis.project.repositories;

import gr.uom.thesis.project.entities.AnalyzedProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyzedProjectFileRepository extends JpaRepository<AnalyzedProjectFile, Long> {
}
