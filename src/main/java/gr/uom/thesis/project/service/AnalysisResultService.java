package gr.uom.thesis.project.service;

import gr.uom.thesis.coverage.TotalCoverageComparator;
import gr.uom.thesis.coverage.TotalCoverageComparatorRepository;
import gr.uom.thesis.dependencies.TotalDependenciesComparator;
import gr.uom.thesis.dependencies.TotalDependenciesRepository;
import gr.uom.thesis.miss.TotalMissComparator;
import gr.uom.thesis.miss.TotalMissComparatorRepository;
import gr.uom.thesis.project.advice.ItemAlreadyAnalyzedException;
import gr.uom.thesis.project.advice.ItemNotFoundException;
import gr.uom.thesis.project.dto.AnalysisResultDto;
import gr.uom.thesis.project.dto.AnalyzedProjectDTO;
import gr.uom.thesis.project.dto.Distance;
import gr.uom.thesis.project.entities.AnalyzedProject;
import gr.uom.thesis.project.repositories.AnalyzedProjectRepository;
import gr.uom.thesis.stmts.TotalStmtsComparator;
import gr.uom.thesis.stmts.TotalStmtsComparatorRepository;
import gr.uom.thesis.utils.AsyncFunctions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisResultService {

    private final TotalCoverageComparatorRepository coverageRepository;
    private final TotalStmtsComparatorRepository stmtsRepository;
    private final TotalMissComparatorRepository missRepository;
    private final TotalDependenciesRepository dependenciesRepository;

    private final AnalyzedProjectRepository projectRepository;

    private final AsyncFunctions asyncFunctions;


    public AnalysisResultDto createComparativeAnalysis(String gitUrl) throws ExecutionException, InterruptedException {
/*        AnalyzedProject projectToBeAnalyzed = projectRepository.findByGitUrl(gitUrl)
                .orElseThrow(() -> new ItemNotFoundException("Project With url: " + gitUrl + " Was Not Found"));*/
        CompletableFuture<ResponseEntity<AnalyzedProjectDTO>> projectByGitUrlFuture =
                asyncFunctions.getProjectByGitUrl(gitUrl);
        ResponseEntity<AnalyzedProjectDTO> analyzedProjectDTOResponseEntity = projectByGitUrlFuture.get();
        log.info(analyzedProjectDTOResponseEntity.toString());
        HttpStatusCode statusCode = analyzedProjectDTOResponseEntity.getStatusCode();
        log.info(statusCode.toString());

        AnalyzedProjectDTO projectToBeAnalyzedDTO = analyzedProjectDTOResponseEntity.getBody();
        assert projectToBeAnalyzedDTO != null;

        if (projectRepository.existsBySha(projectToBeAnalyzedDTO.getSha()))
            throw new ItemAlreadyAnalyzedException(projectToBeAnalyzedDTO.getName());

        AnalyzedProject analyzedProject = new AnalyzedProject();
        analyzedProject.setName(projectToBeAnalyzedDTO.getName());
        analyzedProject.setDependencies(projectToBeAnalyzedDTO.getDependencies());
        analyzedProject.setDirectory(projectToBeAnalyzedDTO.getDirectory());
        analyzedProject.setOwner(projectToBeAnalyzedDTO.getOwner());
        analyzedProject.setDependenciesCounter(projectToBeAnalyzedDTO.getDependenciesCounter());
//        analyzedProject.setFiles(projectToBeAnalyzedDTO.getFiles());
        analyzedProject.setGitUrl(projectToBeAnalyzedDTO.getGitUrl());
        analyzedProject.setSha(projectToBeAnalyzedDTO.getSha());
        analyzedProject.setTotalCoverage(projectToBeAnalyzedDTO.getTotalCoverage());
        analyzedProject.setTotalMiss(projectToBeAnalyzedDTO.getTotalMiss());
        analyzedProject.setTotalStmts(projectToBeAnalyzedDTO.getTotalStmts());

        return compareMetrics(analyzedProject);
    }

    private AnalysisResultDto compareMetrics(AnalyzedProject projectToBeAnalyzed) {
        List<AnalyzedProject> analyzedProjectList = projectRepository.findAll(); //TODO add pagination
        List<Distance> distances = new ArrayList<>();

        projectRepository.save(projectToBeAnalyzed);

        log.info("analyzedProjectList size " + analyzedProjectList.size());
        if (analyzedProjectList.isEmpty()) {
            pairwiseComparison(projectToBeAnalyzed, projectToBeAnalyzed, distances);

            return AnalysisResultDto.builder()
                    .projectName(projectToBeAnalyzed.getName())
                    .distance(distances)
                    .build();
        }

        for (AnalyzedProject analyzedProject : analyzedProjectList)
            pairwiseComparison(projectToBeAnalyzed, analyzedProject, distances);

        return AnalysisResultDto.builder()
                .projectName(projectToBeAnalyzed.getName())
                .distance(distances)
                .build();
    }

    private void pairwiseComparison(AnalyzedProject projectToBeAnalyzed, AnalyzedProject analyzedProject, List<Distance> distances) {
        log.info(analyzedProject.getName());

        int totalStmts = projectToBeAnalyzed.getTotalStmts();
        int totalMiss = projectToBeAnalyzed.getTotalMiss();
        int totalCoverage = projectToBeAnalyzed.getTotalCoverage();
        int totalDependencies = projectToBeAnalyzed.getDependenciesCounter();

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

        int currentDependenciesDifference = totalDependencies - analyzedProject.getDependenciesCounter();
        TotalDependenciesComparator dependenciesComparator = TotalDependenciesComparator.builder()
                .analyzedProject1(analyzedProject)
                .analyzedProject2(projectToBeAnalyzed)
                .totalDependenciesDifference(currentDependenciesDifference)
                .build();
        dependenciesRepository.save(dependenciesComparator);

        distances.add(Distance.builder()
                .projectName(analyzedProject.getName())
                .coverageDifference(coverageComparator.getTotalCoverageDifference())
                .missDifference(missComparator.getTotalMissDifference())
                .stmtsDifference(stmtsComparator.getTotalStmtsDifference())
                .build());
    }

    public AnalysisResultDto getAnalyzedProject(String gitUrl) {
        AnalyzedProject analyzedProject = projectRepository.findByGitUrl(gitUrl).orElseThrow(() -> new ItemNotFoundException(gitUrl + " Not Found"));

        List<TotalCoverageComparator> analyzedProjectCoverage = coverageRepository.getAnalyzedProject(analyzedProject);
        List<TotalDependenciesComparator> analyzedProjectDependencies = dependenciesRepository.getAnalyzedProject(analyzedProject);
        List<TotalStmtsComparator> analyzedProjectStmts = stmtsRepository.getAnalyzedProject(analyzedProject);
        List<TotalMissComparator> analyzedProjectMiss = missRepository.getAnalyzedProject(analyzedProject);
        List<TotalDependenciesComparator> analyzedProjectDeps = dependenciesRepository.getAnalyzedProject(analyzedProject);

        //Map
        List<Distance> distances = new ArrayList<>();

        computeCoverage(analyzedProject, analyzedProjectCoverage, distances);

        computeMiss(analyzedProjectMiss, distances);

        computeStmts(analyzedProjectStmts, distances);

        computeDependencies(analyzedProjectDependencies, distances);

        return AnalysisResultDto.builder()
                .projectName(analyzedProject.getName())
                .distance(distances)
                .build();
    }

    private static void computeDependencies(List<TotalDependenciesComparator> analyzedProjectDependencies, List<Distance> distances) {
        for (TotalDependenciesComparator dependenciesComparator : analyzedProjectDependencies) {
            Distance distance = distances.stream()
                    .filter(d -> d.getProjectName().equals(dependenciesComparator.getAnalyzedProject1().getName())
                            || d.getProjectName().equals(dependenciesComparator.getAnalyzedProject2().getName()))
                    .findFirst().orElseThrow(() -> new ItemNotFoundException("Not Found"));
            if (dependenciesComparator.getAnalyzedProject1().getName().equals(distance.getProjectName())) {
                distance.setDependenciesDifference(dependenciesComparator.getTotalDependenciesDifference());
            } else {
                distance.setDependenciesDifference(dependenciesComparator.getTotalDependenciesDifference() * (-1));
            }
        }
    }

    private static void computeStmts(List<TotalStmtsComparator> analyzedProjectStmts, List<Distance> distances) {
        for (TotalStmtsComparator stmtsComparator : analyzedProjectStmts) {
            Distance distance = distances.stream()
                    .filter(d -> d.getProjectName().equals(stmtsComparator.getAnalyzedProject1().getName())
                            || d.getProjectName().equals(stmtsComparator.getAnalyzedProject2().getName()))
                    .findFirst().orElseThrow(() -> new ItemNotFoundException("Not Found"));
            if (stmtsComparator.getAnalyzedProject1().getName().equals(distance.getProjectName())) {
                distance.setStmtsDifference(stmtsComparator.getTotalStmtsDifference());
            } else {
                distance.setStmtsDifference(stmtsComparator.getTotalStmtsDifference() * (-1));
            }
        }
    }

    private static void computeMiss(List<TotalMissComparator> analyzedProjectMiss, List<Distance> distances) {
        for (TotalMissComparator missComparator : analyzedProjectMiss) {
            Distance distance = distances.stream()
                    .filter(d -> d.getProjectName().equals(missComparator.getAnalyzedProject1().getName())
                            || d.getProjectName().equals(missComparator.getAnalyzedProject2().getName()))
                    .findFirst().orElseThrow(() -> new ItemNotFoundException("Not Found"));

            if (missComparator.getAnalyzedProject1().getName().equals(distance.getProjectName())) {
                distance.setMissDifference(missComparator.getTotalMissDifference());
            } else {
                distance.setMissDifference(missComparator.getTotalMissDifference() * (-1));
            }
        }
    }

    private static void computeCoverage(AnalyzedProject analyzedProject, List<TotalCoverageComparator> analyzedProjectCoverage, List<Distance> distances) {
        for (TotalCoverageComparator coverageComparator : analyzedProjectCoverage) {
            Distance distance = Distance.builder().build();
            if (coverageComparator.getAnalyzedProject1().getName().equals(analyzedProject.getName())) {
                distance.setProjectName(coverageComparator.getAnalyzedProject2().getName());
                distance.setCoverageDifference(coverageComparator.getTotalCoverageDifference());
            } else {
                distance.setProjectName(coverageComparator.getAnalyzedProject1().getName());
                distance.setCoverageDifference(coverageComparator.getTotalCoverageDifference() * (-1));
            }
            distances.add(distance);
        }
    }
}
