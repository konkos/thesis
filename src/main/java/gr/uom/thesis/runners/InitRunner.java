package gr.uom.thesis.runners;

import gr.uom.thesis.project.AnalyzedProjectDTO;
import gr.uom.thesis.utils.AsyncFunctions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitRunner implements CommandLineRunner {

    private final AsyncFunctions asyncFunctions;

    public InitRunner(AsyncFunctions asyncFunctions) {
        this.asyncFunctions = asyncFunctions;
    }

    @Override
    public void run(String... args) throws Exception {
        AnalyzedProjectDTO body = asyncFunctions.getProjectByGitUrl("https://github.com/niekwit/pyseqtools").get().getBody();
        assert body != null;
        log.info(body.toString());
    }

}
