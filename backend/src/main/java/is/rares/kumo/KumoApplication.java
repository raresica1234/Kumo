package is.rares.kumo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KumoApplication {
    public static void main(String[] args) {
        SpringApplication.run(KumoApplication.class, args);
    }
}
