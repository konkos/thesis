package gr.uom.thesis.runners;

import com.google.gson.Gson;
import gr.uom.thesis.comments.TotalCommentsComparator;
import gr.uom.thesis.comments.TotalCommentsRepository;
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
import gr.uom.thesis.project.dto.AnalyzedProjectFileDTO;
import gr.uom.thesis.project.dto.Distance;
import gr.uom.thesis.project.entities.AnalyzedProject;
import gr.uom.thesis.project.entities.AnalyzedProjectFile;
import gr.uom.thesis.project.entities.Comment;
import gr.uom.thesis.project.repositories.AnalyzedProjectFileRepository;
import gr.uom.thesis.project.repositories.AnalyzedProjectRepository;
import gr.uom.thesis.stmts.TotalStmtsComparator;
import gr.uom.thesis.stmts.TotalStmtsComparatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(1)
public class NoExternalServiceRunner implements CommandLineRunner {

    private final AnalyzedProjectRepository projectRepository;
    private final AnalyzedProjectFileRepository analyzedProjectFileRepository;


    @Override
    public void run(String... args) throws Exception {

        String json = getJson();
        var arr = new Gson().fromJson(json, AnalyzedProjectDTO[].class);
        var projectDTOS = List.of(arr);
        log.info("List size {}", projectDTOS.size());
        log.info("List type {}", projectDTOS.getClass().getName());
        log.info("List [0] {}", projectDTOS.get(0).getClass().getName());
        log.info("OBJ [0] {}", projectDTOS.get(0));

        projectDTOS.forEach(this::saveProject);
    }


    private void saveProject(AnalyzedProjectDTO projectToBeAnalyzedDTO) {
        AnalyzedProject analyzedProject = new AnalyzedProject();
        analyzedProject.setName(projectToBeAnalyzedDTO.getName());

        analyzedProject.setDependencies(projectToBeAnalyzedDTO.getDependencies());
        analyzedProject.setDirectory(projectToBeAnalyzedDTO.getDirectory());
        analyzedProject.setOwner(projectToBeAnalyzedDTO.getOwner());
        analyzedProject.setDependenciesCounter(projectToBeAnalyzedDTO.getDependenciesCounter());
        analyzedProject.setGitUrl(projectToBeAnalyzedDTO.getGitUrl());
        analyzedProject.setSha(projectToBeAnalyzedDTO.getSha());
        analyzedProject.setTotalCoverage(projectToBeAnalyzedDTO.getTotalCoverage());
        analyzedProject.setTotalMiss(projectToBeAnalyzedDTO.getTotalMiss());
        analyzedProject.setTotalStmts(projectToBeAnalyzedDTO.getTotalStmts());

        List<AnalyzedProjectFileDTO> filesDTO = projectToBeAnalyzedDTO.getFiles();

        List<AnalyzedProjectFile> listofProjectFiles = filesDTO.stream().map(dto -> AnalyzedProjectFile.builder()
                        .name(dto.getName())
                        .stmts(dto.getStmts())
                        .miss(dto.getMiss())
                        .coverage(dto.getCoverage())
                        .rating(dto.getRating())
                        .previousRating(dto.getPreviousRating())
                        .similarity(dto.getSimilarity())
                        .projectName(dto.getProjectName())
                        .project(analyzedProject)
                        .comments(dto.getComments().stream()
                                .map(commentDTO -> Comment.builder().comment(commentDTO.getComment()).build()).toList())
                        .build())
                .toList();
//        analyzedProjectFileRepository.saveAll(listofProjectFiles);
        analyzedProject.setFiles(listofProjectFiles);
        projectRepository.save(analyzedProject);
    }

    private String getJson() {

        String contents;
        try (InputStream inputStream = getClass().getResourceAsStream("/results.json");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            contents = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            log.error("EXCEPTION AT GETJSON {}", e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("CONTENTS {}", contents.substring(0, 10));
        return contents;
    }


}
