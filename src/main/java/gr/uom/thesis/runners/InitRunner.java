package gr.uom.thesis.runners;

import gr.uom.thesis.coverage.TotalCoverageComparator;
import gr.uom.thesis.coverage.TotalCoverageComparatorRepository;
import gr.uom.thesis.project.dto.AnalyzedProjectDTO;
import gr.uom.thesis.project.entities.AnalyzedProject;
import gr.uom.thesis.project.entities.AnalyzedProjectFile;
import gr.uom.thesis.project.repositories.AnalyzedProjectFileRepository;
import gr.uom.thesis.project.repositories.AnalyzedProjectRepository;
import gr.uom.thesis.utils.AsyncFunctions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class InitRunner implements CommandLineRunner {

    private final AsyncFunctions asyncFunctions;

    private final AnalyzedProjectRepository analyzedProjectRepository;

    private final AnalyzedProjectFileRepository analyzedProjectFileRepository;

    private final TotalCoverageComparatorRepository coverageComparatorRepository;

    public InitRunner(AsyncFunctions asyncFunctions, AnalyzedProjectRepository analyzedProjectRepository, AnalyzedProjectFileRepository analyzedProjectFileRepository, TotalCoverageComparatorRepository coverageComparatorRepository) {
        this.asyncFunctions = asyncFunctions;
        this.analyzedProjectRepository = analyzedProjectRepository;
        this.analyzedProjectFileRepository = analyzedProjectFileRepository;
        this.coverageComparatorRepository = coverageComparatorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        AnalyzedProjectDTO dto = asyncFunctions
                .getProjectByGitUrl("https://github.com/niekwit/pyseqtools").get().getBody();
        assert dto != null;

        AnalyzedProject analyzedProject = new AnalyzedProject();
        BeanUtils.copyProperties(dto, analyzedProject);
        log.info(dto.toString());

        assert dto.getFiles() != null;

        int dtoTotalCoverage = dto.getTotalCoverage();

        List<AnalyzedProject> allProjects = analyzedProjectRepository.findAll();

        for (AnalyzedProject project : allProjects) {
            int currentTotalCoverage = project.getTotalCoverage();
            int totalCoverageDifference = currentTotalCoverage - dtoTotalCoverage;

            TotalCoverageComparator comparator = TotalCoverageComparator.builder()
                    .analyzedProject1(project)
                    .analyzedProject2(analyzedProject)
                    .totalCoverageDifference(totalCoverageDifference)
                    .build();

            coverageComparatorRepository.save(comparator);

        }

        /*
         * For each analyzed project add to Total*ComparatorRepository
         * Λειτουργεί σαν "πίνακας γειτνίασης"
         * */


    }

}
