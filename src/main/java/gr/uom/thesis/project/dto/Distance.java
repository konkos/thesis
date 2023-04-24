package gr.uom.thesis.project.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Distance {
    private String projectName;
    private int coverageDifference;
    private int stmtsDifference;
    private int missDifference;
    private int dependenciesDifference;
}