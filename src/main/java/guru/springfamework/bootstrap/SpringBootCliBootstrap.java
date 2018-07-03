package guru.springfamework.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
@Slf4j
public class SpringBootCliBootstrap implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("Running in Spring Boot Command line runner...");
    }
}
