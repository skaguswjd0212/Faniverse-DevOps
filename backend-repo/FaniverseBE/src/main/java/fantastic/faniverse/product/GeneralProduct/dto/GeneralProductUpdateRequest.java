package fantastic.faniverse.product.GeneralProduct.dto;

import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralProductUpdateRequest {

    @NotEmpty(message = "상품명 입력은 필수입니다.")
    private String title;

    @NotEmpty(message = "카테고리 입력은 필수입니다.")
    private String category;

    @NotEmpty(message = "상품 설명 입력은 필수입니다.")
    private String content;

    @NotNull(message = "가격 입력은 필수입니다.")
    private double price;

    private Long productId;
    private MultipartFile image;
    private Long sellerId;
    private String imageUrl;

    // 기존 GeneralProduct 엔티티를 업데이트하기 위한 메서드
    public void updateEntity(GeneralProduct generalProduct) {
        generalProduct.setTitle(title);
        generalProduct.setCategory(category);
        generalProduct.setContent(content);
        generalProduct.setPrice(price);
        generalProduct.setImageUrl(imageUrl);
    }
}
