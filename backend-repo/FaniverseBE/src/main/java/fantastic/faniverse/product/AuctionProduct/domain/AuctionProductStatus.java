package fantastic.faniverse.product.AuctionProduct.domain;

import lombok.Getter;

@Getter
public enum AuctionProductStatus {
    BID("경매 중"),
    FAIL("경매무산"),
    SOLD("경매완료"),
    PENDING("입금 대기");

    private final String value;

    AuctionProductStatus(String value) {
        this.value = value;
    }
}
