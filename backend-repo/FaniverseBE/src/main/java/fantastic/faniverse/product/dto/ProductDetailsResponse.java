package fantastic.faniverse.product.dto;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.AuctionProduct.dto.AuctionBidResponse;
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
public class ProductDetailsResponse {
        private Long productId;
        private Long sellerId;
        private String userName;
        private String imageUrl;
        private String title;
        private String category;
        private String content;
        private double startingPrice;
        private LocalDateTime endDate;
        private String status;
        private double finalPrice;
        private double price;
        private LocalDateTime createdAt;
        private AuctionBidResponse winningBid;

        //@Builder
        public ProductDetailsResponse(Product product) {
                this.productId = product.getId();
                this.sellerId = product.getSeller().getId();
                this.imageUrl = product.getImageUrl();
                this.category = product.getCategory();
                this.content = product.getContent();
                this.userName = product.getSeller().getUsername();
                this.title = product.getTitle();

                if (product instanceof GeneralProduct generalProduct) {
                        this.price = generalProduct.getPrice();
                        this.status = generalProduct.getGeneralProductStatus().getValue();
                } else if (product instanceof AuctionProduct auctionProduct) {
                        this.startingPrice = auctionProduct.getStartingPrice();
                        this.endDate = auctionProduct.getEndDate();
                        this.status = auctionProduct.getAuctionProductStatus().getValue();
                        this.finalPrice = auctionProduct.getFinalPrice();
                        if (auctionProduct.getWinningBid() != null) {
                                this.winningBid = new AuctionBidResponse(auctionProduct.getWinningBid());
                        }
                }
        }
}
