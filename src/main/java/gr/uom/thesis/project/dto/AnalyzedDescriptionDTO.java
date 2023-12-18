package gr.uom.thesis.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public record AnalyzedDescriptionDTO(List<String> analysis) {
}
