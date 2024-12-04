package fantastic.faniverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class FaniverseApplication {

    public static void main(String[] args) {
        // 환경 변수를 직접 시스템 속성으로 설정
        System.setProperty("SPRING_DATASOURCE_URL", System.getenv("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", System.getenv("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATASOURCE_PASSWORD", System.getenv("SPRING_DATASOURCE_PASSWORD"));
        System.setProperty("GCP_STORAGE_CREDENTIALS_JSON", System.getenv("GCP_STORAGE_CREDENTIALS_JSON"));
        System.setProperty("GCP_PROJECT_ID", System.getenv("GCP_PROJECT_ID"));
        System.setProperty("GCP_BUCKET", System.getenv("GCP_BUCKET"));

        // Spring Boot 애플리케이션 실행
        SpringApplication.run(FaniverseApplication.class, args);
    }
}
