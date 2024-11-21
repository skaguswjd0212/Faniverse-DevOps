package fantastic.faniverse.product.ProductImage;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final Storage storage; // 이미 주입된 Storage 빈을 사용
    private final String bucketName = "faniverse-bucket";
    private static final Logger LOGGER = Logger.getLogger(ImageUploadService.class.getName());

    public String uploadImage(ImageUploadRequest request) throws IOException {
        // Null 체크 추가
        if (request == null) {
            throw new IllegalArgumentException("ImageUploadRequest cannot be null");
        }

        if (request.getFile() == null || request.getFile().isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        LOGGER.info("Uploading file: " + request.getName());

        // BlobId와 BlobInfo를 사용하여 업로드할 파일 정보 설정
        BlobId blobId = BlobId.of(bucketName, request.getName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(request.getFile().getContentType())
                .build();

        // GCS에 파일 업로드
        storage.create(blobInfo, request.getFile().getBytes());

        // 업로드된 파일의 URL 생성
        String imageUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, request.getName());

        return imageUrl;
    }
}
