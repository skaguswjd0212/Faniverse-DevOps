package fantastic.faniverse.product.ProductImage;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Setter
@Getter
public class ImageUploadRequest {
    private String name;
    private MultipartFile file;
}
