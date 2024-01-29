package gr.uom.thesis.project.service;

import gr.uom.thesis.project.advice.ItemNotFoundException;
import gr.uom.thesis.project.dto.AnalyzedDescriptionDTO;
import gr.uom.thesis.project.entities.AnalyzedProject;
import gr.uom.thesis.project.entities.ProjectCategory;
import gr.uom.thesis.project.repositories.AnalyzedProjectRepository;
import gr.uom.thesis.utils.AsyncFunctions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class AnalysisResultDomainService {

    private final RestTemplate restTemplate;
    private final AsyncFunctions asyncFunctions;
    private final AnalyzedProjectRepository projectRepository;

    @Value("${variables.github_api.url}")
    private String githubUrl;

    @Transactional
    public List<ProjectCategory> getProjectDomain(String projectName) {
        AnalyzedProject analyzedProject = projectRepository.findByName(projectName)
                .orElseThrow(() -> new ItemNotFoundException("Project Not Found"));

        String query = fetchDataFromGithub(analyzedProject);

        // Fetch categories from text classification api
        AnalyzedDescriptionDTO analyzedDescriptionDTO = getProjectCategories(query);

        List<ProjectCategory> analysisResult = analyzedDescriptionDTO.analysis();
        analyzedProject.setCategories(analysisResult);
        projectRepository.save(analyzedProject);

        return analysisResult;
    }

    private AnalyzedDescriptionDTO getProjectCategories(String query) {
        AnalyzedDescriptionDTO analyzedDescriptionDTO = asyncFunctions.getProjectCategories(query).join().getBody();
        if (analyzedDescriptionDTO == null) throw new ItemNotFoundException("Analysis Not Found");
        return analyzedDescriptionDTO;
    }

    private String fetchDataFromGithub(AnalyzedProject analyzedProject) {

        String projectOwner = analyzedProject.getOwner();
        String projectName = analyzedProject.getName();
        String completeGithubUrl = githubUrl + projectOwner + "/" + projectName;

        //fetch Data from GitHub api
        GithubProjectRequestDTO currentlyAnalyzedProjects = restTemplate.getForObject(completeGithubUrl, GithubProjectRequestDTO.class);
        if (currentlyAnalyzedProjects == null) throw new ItemNotFoundException("Project Not Found");

        String description = currentlyAnalyzedProjects.getDescription();
        String name = currentlyAnalyzedProjects.getName();
        String reduce = Arrays.stream(currentlyAnalyzedProjects.getTopics()).reduce("", AnalysisResultDomainService::apply);
        return name + "\n" + reduce + "\n" + description;
    }

    private static String apply(String string, String str) {
        return string.concat("\n" + str);
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    static class GithubProjectRequestDTO {
        private String name;
        private String description;
        private String[] topics;
    }

}
