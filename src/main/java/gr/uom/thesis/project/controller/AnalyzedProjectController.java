package gr.uom.thesis.project.controller;

import gr.uom.thesis.project.dto.AnalysisResultDto;
import gr.uom.thesis.project.service.AnalysisResultService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/analyze")
public class AnalyzedProjectController {

    private final AnalysisResultService analysisResultService;

    public AnalyzedProjectController(AnalysisResultService analysisResultService) {
        this.analysisResultService = analysisResultService;
    }

    @PostMapping("/project")
    public AnalysisResultDto getComparativeAnalysis(@RequestParam String gitUrl) throws ExecutionException, InterruptedException {
        return analysisResultService.createComparativeAnalysis(gitUrl);
    }
}
