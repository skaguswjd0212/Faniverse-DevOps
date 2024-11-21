package fantastic.faniverse.config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleCloudConfig {

    @Bean
    public Storage googleCloudStorage() {
        // Google Cloud Storage 클라이언트 빈을 생성
        return StorageOptions.getDefaultInstance().getService();
    }
}
