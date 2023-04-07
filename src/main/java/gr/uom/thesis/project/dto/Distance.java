package gr.uom.thesis.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Distance {
    private String projectName;
    private int coverageDifference;
    private int stmtsDifference;
    private int missDifference;
}