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
        // 환경 변수에서 값을 읽어 시스템 속성으로 설정
        setSystemProperty("SPRING_DATASOURCE_URL");
        setSystemProperty("SPRING_DATASOURCE_USERNAME");
        setSystemProperty("SPRING_DATASOURCE_PASSWORD");
        setSystemProperty("GCP_BUCKET");
        setSystemProperty("GCP_PROJECT_ID");
        setSystemProperty("GCP_STORAGE_CREDENTIALS_JSON");

        SpringApplication.run(FaniverseApplication.class, args);
    }

    private static void setSystemProperty(String key) {
        String value = System.getenv(key); // 환경 변수 읽기
        if (value != null) {
            System.setProperty(key, value); // null이 아닌 경우만 시스템 속성 설정
        }
    }
}
