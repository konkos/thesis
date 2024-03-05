package gr.uom.thesis.utils;

import gr.uom.thesis.project.dto.AnalyzedDescriptionDTO;
import gr.uom.thesis.project.dto.AnalyzedProjectDTO;
import gr.uom.thesis.project.dto.TextClassificationRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;


@Component
@Slf4j
public class AsyncFunctions {

    @Value("${variables.sba.url}")
    private String sbaURL;

    @Value("${variables.text_classification.url}")
    private String textClassificationURL;

    private final RestTemplate restTemplate;

    public AsyncFunctions(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<ResponseEntity<AnalyzedProjectDTO>> getProjectByGitUrl(String gitUrl, String branch) {
        log.info("sba URL: {}", sbaURL);
        URI uri = URI.create(sbaURL + "/project_analysis?gitUrl=" + gitUrl + "&branch=" + branch);
        log.info("URI: {}", uri);
        ResponseEntity<AnalyzedProjectDTO> response = restTemplate.postForEntity(uri, HttpResponse.BodyHandlers.ofString(), AnalyzedProjectDTO.class);
        return CompletableFuture.completedFuture(response);
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<ResponseEntity<AnalyzedDescriptionDTO>> getProjectCategories(String text) {

        URI uri = URI.create(textClassificationURL);
        log.info(uri.toString());
        ResponseEntity<AnalyzedDescriptionDTO> forEntity = restTemplate.postForEntity(uri,
                new TextClassificationRequestDTO(text), AnalyzedDescriptionDTO.class);
        return CompletableFuture.completedFuture(forEntity);
    }

}
