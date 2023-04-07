package gr.uom.thesis.project.service;

import gr.uom.thesis.coverage.TotalCoverageComparatorRepository;
import gr.uom.thesis.miss.TotalMissComparatorRepository;
import gr.uom.thesis.project.dto.AnalysisResultDto;
import gr.uom.thesis.stmts.TotalStmtsComparatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisResultService {

    private final TotalCoverageComparatorRepository coverageRepository;
    private final TotalStmtsComparatorRepository stmtsRepository;
    private final TotalMissComparatorRepository missRepository;


    public AnalysisResultDto getComparativeAnalysis(String gitUrl) {
        throw new RuntimeException("Not Implemented");
    }
}
