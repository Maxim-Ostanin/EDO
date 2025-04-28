package edo_public_api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"edo_service", "edo_public_api"})
public class EdoPublicApplication {
    public static void main(String[] args) {
        SpringApplication.run(EdoPublicApplication.class, args);
    }
}
