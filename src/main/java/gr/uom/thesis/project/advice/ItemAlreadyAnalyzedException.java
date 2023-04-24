package gr.uom.thesis.project.advice;

public class ItemAlreadyAnalyzedException extends RuntimeException {
    private String projectName;

    public ItemAlreadyAnalyzedException(String projectName) {
        super(projectName + " has been analyzed");
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }
}
