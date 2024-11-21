package fantastic.faniverse.product.AuctionProduct.dto;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionBid;

public class AuctionBidResponse {
    private Long id;
    private double bidAmount;
    private String bidderName;

    // 생성자
    public AuctionBidResponse(AuctionBid bid) {
        this.id = bid.getId();
        this.bidAmount = bid.getBidAmount();
        this.bidderName = bid.getUser().getUsername(); // bidder의 이름만 포함
    }
}
