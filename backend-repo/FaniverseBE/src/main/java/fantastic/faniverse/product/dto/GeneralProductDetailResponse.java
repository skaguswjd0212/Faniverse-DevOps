package fantastic.faniverse.product.dto;

import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GeneralProductDetailResponse {
    private Long productId;
    private Long sellerId;
    private String imageUrl;
    private String title;
    private String category;
    private String content;
    private Double price;

    public GeneralProductDetailResponse(Product product) {
        this.productId = product.getId();
        this.imageUrl = product.getImageUrl();
        this.category = product.getCategory();
        this.content = product.getContent();
        this.sellerId = product.getSeller().getId();
        this.title = product.getTitle();

        if (product instanceof GeneralProduct generalProduct) {
            this.price = generalProduct.getPrice();
        }
    }
}
