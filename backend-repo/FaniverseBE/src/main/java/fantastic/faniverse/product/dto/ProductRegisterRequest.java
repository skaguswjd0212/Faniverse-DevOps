package fantastic.faniverse.product.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class ProductRegisterRequest {
    @NotEmpty(message = "상품명 입력은 필수입니다.")
    private String title;
    // 카테고리를 반환하는 메서드
    @NotEmpty(message = "카테고리 입력은 필수입니다.")
    private String category;
    // 내용을 반환하는 메서드
    @NotEmpty(message = "상품 설명 입력은 필수입니다.")
    private String content;

    private MultipartFile image;

    private String imageUrl;
}
