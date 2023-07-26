package gr.uom.thesis.utils.kmeans;

import gr.uom.thesis.project.entities.AnalyzedProject;
import gr.uom.thesis.project.repositories.AnalyzedProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElbowFinder {

    @Autowired
    private static AnalyzedProjectRepository projectRepository;

    //sum of square differences
    public static double sse(Map<Centroid, List<Record>> clustered, Distance distance) {
        double sum = 0;
        for (Map.Entry<Centroid, List<Record>> entry : clustered.entrySet()) {
            Centroid centroid = entry.getKey();
            for (Record record : entry.getValue()) {
                double d = distance.calculate(centroid.getCoordinates(), record.getFeatures());
                sum += Math.pow(d, 2);
            }
        }

        return sum;
    }

    public static List<Double> sseForEachK(int kmax) {

        List<AnalyzedProject> projects = projectRepository.findAll();
        List<Record> records = new ArrayList<>();

        for (AnalyzedProject project : projects) {
            int dependenciesCounter = project.getDependenciesCounter();
            int totalCoverage = project.getTotalCoverage();
            int totalMiss = project.getTotalMiss();
            int totalStmts = project.getTotalStmts();

            Map<String, Double> features = new HashMap<>();
            features.put("dependenciesCounter", (double) dependenciesCounter);
            features.put("coverage", (double) totalCoverage);
            features.put("stmts", (double) totalStmts);
            features.put("miss", (double) totalMiss);

            records.add(new Record(project.getName(), features));
        }

        Distance distance = new EuclideanDistance();
        List<Double> sumOfSquaredErrors = new ArrayList<>();
        //TODO use parallel execution
        for (int k = 2; k <= kmax; k++) {
            Map<Centroid, List<Record>> clusters = KMeans.fit(records, k, distance, 1000);
            double sse = sse(clusters, distance);
            sumOfSquaredErrors.add(sse);
        }
        return sumOfSquaredErrors;

    }
}
