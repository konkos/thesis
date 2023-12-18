package gr.uom.thesis.project.controller;

import gr.uom.thesis.project.service.AnalysisResultDomainService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorize")
@AllArgsConstructor
public class ProjectCategoryController {

    private final AnalysisResultDomainService analysisResultDomainService;

    @GetMapping
    public List<String> getProjectTags(@RequestParam String projectName) {
        return analysisResultDomainService.getProjectDomain(projectName);
    }
}
