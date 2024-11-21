package fantastic.faniverse.product.AuctionProduct.dto;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.dto.ProductRegisterRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionProductRegisterRequest extends ProductRegisterRequest {
    @NotEmpty(message = "시작 가격 입력은 필수입니다.")
    private double startingPrice;

    @NotEmpty(message = "경매 마감 기한 입력은 필수입니다.")
    private LocalDateTime endDate;

    public AuctionProduct toAuctionProductEntity(String imageUrl) {
        return AuctionProduct.builder()
                .title(getTitle())
                .category(getCategory())
                .content(getContent())
                .startingPrice(getStartingPrice())
                .endDate(getEndDate())
                .auctionProductStatus(AuctionProductStatus.BID)
                .imageUrl(imageUrl)
                .build();
    }
}
