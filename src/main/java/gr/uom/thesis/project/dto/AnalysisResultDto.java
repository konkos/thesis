package gr.uom.thesis.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
public class AnalysisResultDto {

    private String projectName;
    private List<Distance> distance;


    @Getter
    @Setter
    class Distance {
        private String projectName;
        private int coverageDifference;
        private int stmtsDifference;
        private int missDifference;
    }
}
