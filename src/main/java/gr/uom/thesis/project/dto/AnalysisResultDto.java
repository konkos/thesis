package gr.uom.thesis.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalysisResultDto {

    private String projectName;
    private List<Distance> distance;

}
