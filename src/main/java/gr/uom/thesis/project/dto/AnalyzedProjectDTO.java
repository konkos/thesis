package gr.uom.thesis.project.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class AnalyzedProjectDTO {

    private String gitUrl;
    private String owner;
    private String name;
    private String directory;
    private List<SingleAnalyzedProjectDto> singleAnalyzedProjectList;


}
