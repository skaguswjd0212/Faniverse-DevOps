package fantastic.faniverse.product.GeneralProduct.dto;

import fantastic.faniverse.product.dto.ProductRegisterRequest;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class GeneralProductRegisterRequest extends ProductRegisterRequest {

    @NotEmpty(message = "가격 입력은 필수입니다.")
    private double price;

    private String imageUrl;

    public GeneralProduct toGeneralProductEntity(String imageUrl) {
        return GeneralProduct.builder()
                .title(getTitle())
                .category(getCategory())
                .content(getContent())
                .price(price)
                .generalProductStatus(GeneralProductStatus.SALE)
                .imageUrl(imageUrl)
                .build();
    }
}
