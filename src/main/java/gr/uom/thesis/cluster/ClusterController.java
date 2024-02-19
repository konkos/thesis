package gr.uom.thesis.cluster;

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

    private final ElbowFinder elbowFinder;

    public ClusterController(ClusterService clusterService, ElbowFinder elbowFinder) {
        this.clusterService = clusterService;
        this.elbowFinder = elbowFinder;
    }

    @PostMapping
    public Map<Centroid, List<Record>> fitData(@RequestParam(value = "k", defaultValue = "5") int k,
                                               @RequestParam(defaultValue = "", required = false) String field) {
        return clusterService.createClusters(k, field);
    }


    @GetMapping
    public List<Double> elbowMethod(@RequestParam(value = "kmax", defaultValue = "16") int kmax) {
        return elbowFinder.sseForEachK(kmax);
    }

}
