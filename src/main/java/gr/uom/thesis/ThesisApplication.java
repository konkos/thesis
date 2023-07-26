package gr.uom.thesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@PropertySource("classpath:application.yml")
public class ThesisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThesisApplication.class, args);
    }

}
