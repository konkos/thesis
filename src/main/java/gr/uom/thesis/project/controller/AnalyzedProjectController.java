package gr.uom.thesis.project.controller;

import gr.uom.thesis.project.dto.AnalysisResultDto;
import gr.uom.thesis.project.service.AnalysisResultService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/analyze")
public class AnalyzedProjectController {

    private final AnalysisResultService analysisResultService;

    public AnalyzedProjectController(AnalysisResultService analysisResultService) {
        this.analysisResultService = analysisResultService;
    }

    @GetMapping("/project")
    public AnalysisResultDto getComparativeAnalysis(@RequestParam String gitUrl, @RequestParam String branch) {
        return analysisResultService.createComparativeAnalysis(gitUrl, branch);
    }

/*    @GetMapping("/project")
    public AnalysisResultDto getAnalyzedProject(@RequestParam String gitUrl) {
        return analysisResultService.getAnalyzedProject(gitUrl);
    }*/
}
