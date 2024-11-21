package fantastic.faniverse;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class FaniverseApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		System.setProperty("GCP_STORAGE_CREDENTIALS_JSON", dotenv.get("GCP_STORAGE_CREDENTIALS_JSON"));
		System.setProperty("GCP_PROJECT_ID", dotenv.get("GCP_PROJECT_ID"));
		System.setProperty("GCP_BUCKET", dotenv.get("GCP_BUCKET"));

		SpringApplication.run(FaniverseApplication.class, args);
	}
}
