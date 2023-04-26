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
    private ArrayList<String> dependencies;
    private int dependenciesCounter;
    private List<AnalyzedProjectFileDTO> files;
    private int totalCoverage;
    private int totalMiss;
    private int totalStmts;
    private String sha;
//    private List<CommentDTO> comments;

}
