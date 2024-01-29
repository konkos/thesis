package gr.uom.thesis.project.dto;

import gr.uom.thesis.project.entities.ProjectCategory;

import java.util.List;


public record AnalyzedDescriptionDTO(List<ProjectCategory> analysis) {
}
