package fantastic.faniverse.product.dto;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@Builder
public class ProductListResponse {
    private Long id;  // 상품 ID (클릭 시 상세 조회용)
    private String title;  // 상품 제목
    private String imageUrl;  // 상품 이미지
    private double price;  // 상품 가격
    private double startingPrice;  // 상품 가격
    private double finalPrice;  // 상품 가격
    private LocalDateTime endDate;  // 경매 종료일 (경매 상품일 경우)
    private String category;

    public ProductListResponse(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.imageUrl = product.getImageUrl();
        this.category = product.getCategory();

        if (product instanceof AuctionProduct auctionProduct) {
            this.startingPrice = auctionProduct.getStartingPrice();
            this.finalPrice = auctionProduct.getFinalPrice();
            this.endDate = auctionProduct.getEndDate();
        } else if (product instanceof GeneralProduct generalProduct) {
            this.price = generalProduct.getPrice();
        }
    }
}
