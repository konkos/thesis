package gr.uom.thesis.cluster;

import gr.uom.thesis.project.repositories.AnalyzedProjectRepository;
import gr.uom.thesis.utils.kmeans.Centroid;
import gr.uom.thesis.utils.kmeans.ElbowFinder;
import gr.uom.thesis.utils.kmeans.Record;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cluster")
public class ClusterController {

    private final ClusterService clusterService;

    public ClusterController(AnalyzedProjectRepository analyzedProjectRepository, ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @PostMapping
    public Map<Centroid, List<Record>> fitData(){
        return clusterService.createClusters();
    }


    @GetMapping
    public List<Double> elbowMethod(@RequestParam(value = "kmax", defaultValue = "16") int kmax){
        return ElbowFinder.sseForEachK(kmax);
    }

}
