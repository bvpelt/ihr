package nl.bsoft.ihr.synchroniseren;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"nl.bsoft.ihr.library", "nl.bsoft.ihr.generated.model"})
@SpringBootApplication
public class SynchroniserenApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynchroniserenApplication.class, args);
    }

}