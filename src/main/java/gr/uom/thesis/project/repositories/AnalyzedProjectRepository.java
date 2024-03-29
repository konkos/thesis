package gr.uom.thesis.project.repositories;

import gr.uom.thesis.project.entities.AnalyzedProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnalyzedProjectRepository extends JpaRepository<AnalyzedProject, Long> {

    Optional<AnalyzedProject> findByGitUrl(String gitUrl);

    Optional<AnalyzedProject> findByName(String name);

    boolean existsBySha(String sha);
}
