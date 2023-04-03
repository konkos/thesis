package gr.uom.thesis.project.dto;

import lombok.*;

import java.io.File;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class AnalyzedProjectFileDTO {


    File firstFile;
    String name;
    int stmts;
    int miss;
    int coverage;
    private List<CommentDTO> comments;
    Double rating;
    Double previousRating;
    Map<String, Double> similarity;
    private AnalyzedProjectDTO project;
    private String projectName;
}
