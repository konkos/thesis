package gr.uom.thesis.stmts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TotalStmtsComparatorRepository extends JpaRepository<TotalStmtsComparator, Long> {


    Optional<TotalStmtsComparator> findByProject1OrProject2();
}
