package gr.uom.thesis.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResultDto {

    private String projectName;
    private List<Distance> distance;

}
