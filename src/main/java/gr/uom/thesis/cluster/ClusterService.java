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

/*    private final TotalCoverageComparatorRepository coverageComparatorRepository;
    private final TotalStmtsComparatorRepository stmtsRepository;
    private final TotalDependenciesRepository dependenciesRepository;
    private final TotalMissComparatorRepository missRepository;*/

    private final AnalyzedProjectRepository projectRepository;

    public ClusterService(AnalyzedProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
//    public ClusterService(TotalCoverageComparatorRepository coverageComparatorRepository, TotalStmtsComparatorRepository stmtsRepository, TotalDependenciesRepository dependenciesRepository, TotalMissComparatorRepository missRepository) {
//        this.coverageComparatorRepository = coverageComparatorRepository;
//        this.stmtsRepository = stmtsRepository;
//        this.dependenciesRepository = dependenciesRepository;
//        this.missRepository = missRepository;
//    }

    public Map<Centroid, List<Record>> createClusters() {

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

        //TODO add elbow method

        return KMeans.fit(records, 5, new EuclideanDistance(), 1000);
    }
}
