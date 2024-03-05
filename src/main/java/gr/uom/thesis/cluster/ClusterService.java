package gr.uom.thesis.cluster;

import gr.uom.thesis.project.entities.AnalyzedProject;
import gr.uom.thesis.project.entities.ProjectCategory;
import gr.uom.thesis.project.repositories.AnalyzedProjectRepository;
import gr.uom.thesis.project.repositories.ProjectCategoryRepository;
import gr.uom.thesis.utils.kmeans.Centroid;
import gr.uom.thesis.utils.kmeans.EuclideanDistance;
import gr.uom.thesis.utils.kmeans.KMeans;
import gr.uom.thesis.utils.kmeans.Record;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClusterService {

    private final AnalyzedProjectRepository projectRepository;
    private final ProjectCategoryRepository categoryRepository;


    public ClusterService(AnalyzedProjectRepository projectRepository, ProjectCategoryRepository categoryRepository) {
        this.projectRepository = projectRepository;
        this.categoryRepository = categoryRepository;
    }


    public Map<Centroid, List<Record>> createClusters(int k, String categoryString) {

        List<AnalyzedProject> projects = getProjectsByCategory(categoryString);

        List<Record> records = new ArrayList<>();

        for (AnalyzedProject project : projects) {
            Map<String, Double> features = getFeatures(project);

            records.add(new Record(project.getName(), features));
        }

        //TODO add elbow method

        return KMeans.fit(records, k, new EuclideanDistance(), 1000);
    }

    private List<AnalyzedProject> getProjectsByCategory(String field) {
        List<AnalyzedProject> projects;
        if (field.isBlank()) projects = projectRepository.findAll();
        else projects = projectRepository.findByCategories_WordLikeIgnoreCase(field);
        return projects;
    }

    private Map<String, Double> getFeatures(AnalyzedProject project) {
        int dependenciesCounter = project.getDependenciesCounter();
        int totalCoverage = project.getTotalCoverage();
        int totalMiss = project.getTotalMiss();
        int totalStmts = project.getTotalStmts();
        int commentsSize = project.getComments().size();
        int numberOfFiles = project.getFiles().size();

        Map<String, Double> features = new HashMap<>();
        features.put("dependenciesCounter", (double) dependenciesCounter);
        features.put("coverage", (double) totalCoverage);
        features.put("stmts", (double) totalStmts);
        features.put("miss", (double) totalMiss);
        features.put("comments", (double) commentsSize);
        features.put("numberOfFiles", (double) numberOfFiles);
        featuresPutCategories(project, features);

        return features;
    }

    private void featuresPutCategories(AnalyzedProject project, Map<String, Double> features) {
        for (ProjectCategory projectCategory : categoryRepository.findAll()) {
            if (project.getCategories().contains(projectCategory))
                features.put(projectCategory.getWord(), 1.0);
            else
                features.put(projectCategory.getWord(), 0.0);
        }
    }
}
