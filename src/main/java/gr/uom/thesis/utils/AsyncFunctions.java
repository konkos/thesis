package gr.uom.thesis.utils;

import gr.uom.thesis.project.AnalyzedProjectDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.CompletableFuture;


@Component
@Slf4j
public class AsyncFunctions {

    @Value("${variables.sba.url}")
    private String sbaURL;

    private final RestTemplate restTemplate;

    public AsyncFunctions(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<ResponseEntity<AnalyzedProjectDTO>> getProjectByGitUrl(String gitUrl) {
        URI uri = URI.create(sbaURL + "/projects/?gitUrl=" + gitUrl);
        ResponseEntity<AnalyzedProjectDTO> response = restTemplate.getForEntity(uri, AnalyzedProjectDTO.class);
        return CompletableFuture.completedFuture(response);
    }

}
