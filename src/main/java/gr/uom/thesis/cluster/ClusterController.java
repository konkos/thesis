package gr.uom.thesis.cluster;

import gr.uom.thesis.utils.kmeans.Centroid;
import gr.uom.thesis.utils.kmeans.ElbowFinder;
import gr.uom.thesis.utils.kmeans.Record;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

/*

Old API

 @PostMapping
    public Map<Centroid, List<Record>> fitData(@RequestParam(value = "k", defaultValue = "5") int k,
                                               @RequestParam(defaultValue = "", required = false) String field) {
        return lusterService.createClusters(k, field);
    }*/

    /*
     * Replace old Api to Return List<List>. Alternatively change Centroid.toString to change output
     * */
    @PostMapping
    public List<List<Record>> fitData(@RequestParam(value = "k", defaultValue = "5") int k,
                                      @RequestParam(defaultValue = "", required = false) String field) {
        Map<Centroid, List<Record>> clusters = clusterService.createClusters(k, field);

        List<List<Record>> result = new ArrayList<>();
//        List<List<Record>> list = clusters.entrySet().stream().map(c -> c.getValue()).toList();
        for (Map.Entry<Centroid, List<Record>> centroid : clusters.entrySet()) {
            result.add(centroid.getValue());
        }
        return result;
    }


    @GetMapping
    public List<Double> elbowMethod(@RequestParam(value = "kmax", defaultValue = "16") int kmax) {
        return elbowFinder.sseForEachK(kmax);
    }

}
