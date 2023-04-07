package gr.uom.thesis.project.service;

import gr.uom.thesis.coverage.TotalCoverageComparator;
import gr.uom.thesis.coverage.TotalCoverageComparatorRepository;
import gr.uom.thesis.miss.TotalMissComparator;
import gr.uom.thesis.miss.TotalMissComparatorRepository;
import gr.uom.thesis.project.dto.AnalysisResultDto;
import gr.uom.thesis.project.dto.AnalyzedProjectDTO;
import gr.uom.thesis.project.dto.Distance;
import gr.uom.thesis.project.entities.AnalyzedProject;
import gr.uom.thesis.project.repositories.AnalyzedProjectRepository;
import gr.uom.thesis.stmts.TotalStmtsComparator;
import gr.uom.thesis.stmts.TotalStmtsComparatorRepository;
import gr.uom.thesis.utils.AsyncFunctions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class AnalysisResultService {

    private final TotalCoverageComparatorRepository coverageRepository;
    private final TotalStmtsComparatorRepository stmtsRepository;
    private final TotalMissComparatorRepository missRepository;

    private final AnalyzedProjectRepository projectRepository;

    private final AsyncFunctions asyncFunctions;


    public AnalysisResultDto createComparativeAnalysis(String gitUrl) throws ExecutionException, InterruptedException {
/*        AnalyzedProject projectToBeAnalyzed = projectRepository.findByGitUrl(gitUrl)
                .orElseThrow(() -> new ItemNotFoundException("Project With url: " + gitUrl + " Was Not Found"));*/
        AnalyzedProjectDTO projectToBeAnalyzedDTO = asyncFunctions
                .getProjectByGitUrl(gitUrl).get().getBody();

        AnalyzedProject analyzedProject = new AnalyzedProject();
        analyzedProject.setDependencies(projectToBeAnalyzedDTO.getDependencies());
        analyzedProject.setDirectory(projectToBeAnalyzedDTO.getDirectory());
        analyzedProject.setOwner(projectToBeAnalyzedDTO.getOwner());
        analyzedProject.setDependenciesCounter(projectToBeAnalyzedDTO.getDependenciesCounter());
//        analyzedProject.setFiles(projectToBeAnalyzedDTO.getFiles());
        analyzedProject.setGitUrl(projectToBeAnalyzedDTO.getGitUrl());
        analyzedProject.setSha(projectToBeAnalyzedDTO.getSha());

        return compareMetrics(analyzedProject);
    }

    private AnalysisResultDto compareMetrics(AnalyzedProject projectToBeAnalyzed) {
        List<AnalyzedProject> analyzedProjectList = projectRepository.findAll(); //TODO add pagination
        List<Distance> distances = new ArrayList<>();

        int totalStmts = projectToBeAnalyzed.getTotalStmts();
        int totalMiss = projectToBeAnalyzed.getTotalMiss();
        int totalCoverage = projectToBeAnalyzed.getTotalCoverage();

        for (AnalyzedProject analyzedProject : analyzedProjectList) {

            int currentStmtsDifference = totalStmts - analyzedProject.getTotalStmts();
            TotalStmtsComparator stmtsComparator = TotalStmtsComparator.builder()
                    .analyzedProject1(analyzedProject)
                    .analyzedProject2(projectToBeAnalyzed)
                    .totalStmtsDifference(currentStmtsDifference)
                    .build();
            stmtsRepository.save(stmtsComparator);

            int currentMissDifference = totalMiss - analyzedProject.getTotalMiss();
            TotalMissComparator missComparator = TotalMissComparator.builder()
                    .analyzedProject1(analyzedProject)
                    .analyzedProject2(projectToBeAnalyzed)
                    .totalMissDifference(currentMissDifference)
                    .build();
            missRepository.save(missComparator);

            int currentCoverageDifference = totalCoverage - analyzedProject.getTotalCoverage();
            TotalCoverageComparator coverageComparator = TotalCoverageComparator.builder()
                    .analyzedProject1(analyzedProject)
                    .analyzedProject2(projectToBeAnalyzed)
                    .totalCoverageDifference(currentCoverageDifference)
                    .build();
            coverageRepository.save(coverageComparator);
            distances.add(Distance.builder()
                    .coverageDifference(coverageComparator.getTotalCoverageDifference())
                    .missDifference(missComparator.getTotalMissDifference())
                    .stmtsDifference(stmtsComparator.getTotalStmtsDifference())
                    .build());
        }
        return AnalysisResultDto.builder()
                .projectName(projectToBeAnalyzed.getName())
                .distance(distances)
                .build();
    }
}
