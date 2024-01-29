package gr.uom.thesis.project.repositories;

import gr.uom.thesis.project.entities.ProjectCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectCategoryRepository extends JpaRepository<ProjectCategory, Long> {

    Optional<ProjectCategory> findByWord(String word);

    Optional<ProjectCategory> findByWordLike(String word);

    Optional<ProjectCategory> findByEntityLike(String entity);
}
