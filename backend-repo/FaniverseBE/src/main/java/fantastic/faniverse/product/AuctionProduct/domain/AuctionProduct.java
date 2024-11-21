package fantastic.faniverse.product.AuctionProduct.domain;

import fantastic.faniverse.Exception.NoBidderException;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import fantastic.faniverse.user.entity.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.*;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AuctionProduct")
@Entity
@DiscriminatorValue("auction_product")
public class AuctionProduct extends Product {

    @Column(name = "auctionProductStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuctionProductStatus auctionProductStatus;

    @Column(name = "startingPrice", nullable = false)
    private double startingPrice;

    @Column(name = "finalPrice")
    private double finalPrice = 0.0;

    @OneToOne
    @Nullable
    @JoinColumn(name = "winningBid")
    private AuctionBid winningBid;

    @Column(name = "endDate", nullable = false)
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "auctionProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuctionBid> bids = new ArrayList<>();

    @Override
    public ProductDetailsResponse toProductDetail() {
        return new ProductDetailsResponse(this);
    }

    public void setAuctionStatus(AuctionProductStatus status) {
        this.auctionProductStatus = status;
    }

    @Override
    public double getPrice() {
        return startingPrice;
    }

    public boolean placeBid(User user, double bidAmount) {
        AuctionBid newBid = new AuctionBid(this, user, bidAmount);

        // 중복된 사용자의 입찰은 허용하지 않음
        boolean userAlreadyBid = bids.stream()
                .anyMatch(bid -> bid.getUser().equals(user));
        if (userAlreadyBid) {
            return false;
        }

        bids.add(newBid);
        return true;
    }

    public boolean isAuctionEnded() {
        return LocalDateTime.now().isAfter(endDate);
    }

    //현재 최고 입찰가
    public AuctionBid getWinningBidNow(AuctionProduct auctionProduct) {
        System.out.println("getWinningBidNow() called for auctionProduct: " + auctionProduct.getId());

        Optional<AuctionBid> highestBid = auctionProduct.getBids().stream()
                .max(Comparator.comparing(AuctionBid::getBidAmount));

        if (highestBid.isPresent()) {
            return highestBid.get();
        } else {
            throw new NoBidderException("현재 입찰자가 없습니다");
        }
    }

    public void endAuction() {
        // 최고 입찰자를 선정
        Optional<AuctionBid> highestBid = bids.stream()
                .max(Comparator.comparing(AuctionBid::getBidAmount));

        if (highestBid.isPresent()) {
            // 최고 입찰자를 winningBid로 설정하고 상태를 PENDING으로 변경
            winningBid = highestBid.get();
            auctionProductStatus = AuctionProductStatus.PENDING;
        } else {
            // 입찰자가 없으면 경매를 실패로 처리
            auctionProductStatus = AuctionProductStatus.FAIL;
            throw new NoBidderException("입찰자가 없어서 경매가 취소됩니다.");
        }
    }

    public void selectNextHighestBid() {
        // 현재 winningBid를 제외한 입찰 중에서 가장 높은 입찰 찾기
        Optional<AuctionBid> nextHighestBid = bids.stream()
                .filter(bid -> !bid.equals(winningBid)) // winningBid와 다른 입찰만 선택
                .max(Comparator.comparing(AuctionBid::getBidAmount));

        if (nextHighestBid.isPresent()) {
            // 새로운 최고 입찰자로 설정
            winningBid = nextHighestBid.get();
        } else {
            // 입찰자가 없으면 경매를 실패로 처리
            this.auctionProductStatus = AuctionProductStatus.FAIL;
            throw new NoBidderException("입찰자가 없어서 경매가 취소됩니다.");
        }
    }

}
