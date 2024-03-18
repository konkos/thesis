package gr.uom.thesis.runners;

import gr.uom.thesis.comments.TotalCommentsComparator;
import gr.uom.thesis.comments.TotalCommentsRepository;
import gr.uom.thesis.coverage.TotalCoverageComparator;
import gr.uom.thesis.coverage.TotalCoverageComparatorRepository;
import gr.uom.thesis.dependencies.TotalDependenciesComparator;
import gr.uom.thesis.dependencies.TotalDependenciesRepository;
import gr.uom.thesis.miss.TotalMissComparator;
import gr.uom.thesis.miss.TotalMissComparatorRepository;
import gr.uom.thesis.project.advice.ItemNotFoundException;
import gr.uom.thesis.project.dto.AnalysisResultDto;
import gr.uom.thesis.project.dto.Distance;
import gr.uom.thesis.project.entities.AnalyzedProject;
import gr.uom.thesis.project.repositories.AnalyzedProjectRepository;
import gr.uom.thesis.stmts.TotalStmtsComparator;
import gr.uom.thesis.stmts.TotalStmtsComparatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(2)
@Slf4j
@Transactional
public class Runner2 implements CommandLineRunner {
    private final AnalyzedProjectRepository projectRepository;
    private final TotalCoverageComparatorRepository coverageRepository;
    private final TotalStmtsComparatorRepository stmtsRepository;
    private final TotalMissComparatorRepository missRepository;
    private final TotalDependenciesRepository dependenciesRepository;
    private final TotalCommentsRepository commentsRepository;

    @Override
    public void run(String... args) throws Exception {
        List<AnalyzedProject> projects = projectRepository.findAll();
        projects.forEach(this::compareMetrics);
    }

    private AnalysisResultDto compareMetrics(AnalyzedProject projectToBeAnalyzed) {
        //TODO add pagination
        List<AnalyzedProject> analyzedProjectList = projectRepository.findAll();
        List<Distance> distances = new ArrayList<>();

        log.info("analyzedProjectList size " + analyzedProjectList.size());
        if (analyzedProjectList.isEmpty()) {
            return handleFirstAnalysisItem(projectToBeAnalyzed, distances);
        }

        for (AnalyzedProject analyzedProject : analyzedProjectList)
            pairwiseComparison(projectToBeAnalyzed, analyzedProject, distances);

        return AnalysisResultDto.builder()
                .projectName(projectToBeAnalyzed.getName())
                .distance(distances)
                .build();
    }

    private AnalysisResultDto handleFirstAnalysisItem(AnalyzedProject projectToBeAnalyzed, List<Distance> distances) {
        pairwiseComparison(projectToBeAnalyzed, projectToBeAnalyzed, distances);

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
        int totalComments = projectToBeAnalyzed.getComments().size();

        int currentStmtsDifference = analyzedProject.getTotalStmts() - totalStmts;
        TotalStmtsComparator stmtsComparator = TotalStmtsComparator.builder()
                .analyzedProject1(analyzedProject)
                .analyzedProject2(projectToBeAnalyzed)
                .totalStmtsDifference(currentStmtsDifference)
                .build();
        stmtsRepository.save(stmtsComparator);

        int currentMissDifference = analyzedProject.getTotalMiss() - totalMiss;
        TotalMissComparator missComparator = TotalMissComparator.builder()
                .analyzedProject1(analyzedProject)
                .analyzedProject2(projectToBeAnalyzed)
                .totalMissDifference(currentMissDifference)
                .build();
        missRepository.save(missComparator);

        int currentCoverageDifference = analyzedProject.getTotalCoverage() - totalCoverage;
        TotalCoverageComparator coverageComparator = TotalCoverageComparator.builder()
                .analyzedProject1(analyzedProject)
                .analyzedProject2(projectToBeAnalyzed)
                .totalCoverageDifference(currentCoverageDifference)
                .build();
        coverageRepository.save(coverageComparator);

        int currentDependenciesDifference = analyzedProject.getDependenciesCounter() - totalDependencies;
        TotalDependenciesComparator dependenciesComparator = TotalDependenciesComparator.builder()
                .analyzedProject1(analyzedProject)
                .analyzedProject2(projectToBeAnalyzed)
                .totalDependenciesDifference(currentDependenciesDifference)
                .build();
        dependenciesRepository.save(dependenciesComparator);

        int commentsDifference = analyzedProject.getComments().size() - totalComments;
        TotalCommentsComparator commentsComparator = TotalCommentsComparator.builder()
                .analyzedProject1(analyzedProject)
                .analyzedProject2(projectToBeAnalyzed)
                .totalCommentsDifference(commentsDifference)
                .build();
        commentsRepository.save(commentsComparator);

        distances.add(Distance.builder()
                .projectName(analyzedProject.getName())
                .coverageDifference(coverageComparator.getTotalCoverageDifference())
                .missDifference(missComparator.getTotalMissDifference())
                .stmtsDifference(stmtsComparator.getTotalStmtsDifference())
                .commentsDifference(commentsComparator.getTotalCommentsDifference())
                .build());
    }

    public AnalysisResultDto getAnalyzedProject(String gitUrl) {
        AnalyzedProject analyzedProject = projectRepository.findByGitUrl(gitUrl).orElseThrow(() -> new ItemNotFoundException(gitUrl + " Not Found"));

        List<TotalCoverageComparator> analyzedProjectCoverage = coverageRepository.getAnalyzedProject(analyzedProject);
        List<TotalDependenciesComparator> analyzedProjectDependencies = dependenciesRepository.getAnalyzedProject(analyzedProject);
        List<TotalStmtsComparator> analyzedProjectStmts = stmtsRepository.getAnalyzedProject(analyzedProject);
        List<TotalMissComparator> analyzedProjectMiss = missRepository.getAnalyzedProject(analyzedProject);
        List<TotalCommentsComparator> analyzedProjectComments = commentsRepository.getAnalyzedProject(analyzedProject);

        //Map
        List<Distance> distances = new ArrayList<>();

        computeCoverage(analyzedProject, analyzedProjectCoverage, distances);

        computeMiss(analyzedProjectMiss, distances);

        computeStmts(analyzedProjectStmts, distances);

        computeDependencies(analyzedProjectDependencies, distances);

        computeComments(analyzedProjectComments, distances);

        return AnalysisResultDto.builder()
                .projectName(analyzedProject.getName())
                .distance(distances)
                .build();
    }

    private void computeComments(List<TotalCommentsComparator> analyzedProjectComments, List<Distance> distances) {
        for (TotalCommentsComparator commentsComparator : analyzedProjectComments) {
            Distance distance = distances.stream()
                    .filter(d -> d.getProjectName().equals(commentsComparator.getAnalyzedProject1().getName())
                            || d.getProjectName().equals(commentsComparator.getAnalyzedProject2().getName()))
                    .findFirst().orElseThrow(() -> new ItemNotFoundException("Not Found"));

            if (commentsComparator.getAnalyzedProject1().getName().equals(distance.getProjectName())) {
                distance.setCommentsDifference(commentsComparator.getTotalCommentsDifference());
            } else {
                distance.setCommentsDifference(commentsComparator.getTotalCommentsDifference() * (-1));
            }

        }
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
