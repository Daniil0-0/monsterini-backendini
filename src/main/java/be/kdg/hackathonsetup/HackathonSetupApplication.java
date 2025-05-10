package be.kdg.hackathonsetup;

import be.kdg.hackathonsetup.config.DotenvInitializer;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class HackathonSetupApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HackathonSetupApplication.class)
                .initializers(new DotenvInitializer())
                .run(args);
    }
}
