package gr.uom.thesis.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SingleAnalyzedProjectDto {
    private List<String> dependencies;
    private int dependenciesCounter;
    private List<AnalyzedProjectFileDTO> files;
    private int totalCoverage;
    private int totalMiss;
    private int totalStmts;
    private String sha;

}