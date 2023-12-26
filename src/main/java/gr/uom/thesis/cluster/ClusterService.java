package gr.uom.thesis.cluster;

import gr.uom.thesis.project.entities.AnalyzedProject;
import gr.uom.thesis.project.repositories.AnalyzedProjectRepository;
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

    public ClusterService(AnalyzedProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    public Map<Centroid, List<Record>> createClusters(int k) {

        List<AnalyzedProject> projects = projectRepository.findAll();
        List<Record> records = new ArrayList<>();

        for (AnalyzedProject project : projects) {
            Map<String, Double> features = getFeatures(project);

            records.add(new Record(project.getName(), features));
        }

        //TODO add elbow method

        return KMeans.fit(records, k, new EuclideanDistance(), 1000);
    }

    private static Map<String, Double> getFeatures(AnalyzedProject project) {
        int dependenciesCounter = project.getDependenciesCounter();
        int totalCoverage = project.getTotalCoverage();
        int totalMiss = project.getTotalMiss();
        int totalStmts = project.getTotalStmts();
        int commentsSize = project.getComments().size();
        int size = project.getFiles().size();

        Map<String, Double> features = new HashMap<>();
        features.put("dependenciesCounter", (double) dependenciesCounter);
        features.put("coverage", (double) totalCoverage);
        features.put("stmts", (double) totalStmts);
        features.put("miss", (double) totalMiss);
        features.put("comments", (double) commentsSize);
        features.put("size", (double) size);
        return features;
    }
}
