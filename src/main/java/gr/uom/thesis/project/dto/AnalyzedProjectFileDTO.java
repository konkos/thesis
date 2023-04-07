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


    private File firstFile;

    private String name;

    private int stmts;

    private int miss;

    private int coverage;

    private List<CommentDTO> comments;

    private Double rating;

    private Double previousRating;

    private Map<String, Double> similarity;

    private AnalyzedProjectDTO project;

    private String projectName;
}
