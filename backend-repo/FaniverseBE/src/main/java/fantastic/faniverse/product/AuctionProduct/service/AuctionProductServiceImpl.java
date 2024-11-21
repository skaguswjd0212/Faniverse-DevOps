package fantastic.faniverse.product.AuctionProduct.service;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionBid;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.AuctionProduct.dto.AuctionProductRegisterRequest;
import fantastic.faniverse.product.AuctionProduct.repository.AuctionBidRepository;
import fantastic.faniverse.product.AuctionProduct.repository.AuctionProductRepository;
import fantastic.faniverse.Exception.ProductNotFoundException;
import fantastic.faniverse.product.ProductImage.ImageUploadService;
import fantastic.faniverse.product.service.ProductService;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuctionProductServiceImpl implements AuctionProductService {
    private final AuctionBidRepository auctionBidRepository;
    private final UserRepository userRepository;
    private final AuctionProductRepository auctionProductRepository;
    private final ImageUploadService imageUploadService;
    private final ProductService productService;

    //경매 상품 등록
    @Override
    public Long saveAuctionProduct(AuctionProductRegisterRequest request, Long userId) throws IOException {
        // 사용자 검증
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new ProductNotFoundException("사용자를 찾을 수 없습니다"));

        // 이미지 업로드 처리 (이미 컨트롤러에서 처리됨)
        String imageUrl = request.getImageUrl();

        // AuctionProduct 엔티티 생성
        AuctionProduct auctionProduct = request.toAuctionProductEntity(imageUrl);

        System.out.println("Image URL before save: " + auctionProduct.getImageUrl());

        auctionProduct.setSeller(seller);  // seller 설정

        // 상품 저장
        auctionProductRepository.save(auctionProduct);

        return auctionProduct.getId(); // 반환할 값 확인
    }

    //경매 상품 상태 전환
    @Override
    public void updateAuctionProductStatus(Long id, AuctionProductStatus status) {
        AuctionProduct auctionProduct = auctionProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction product not found"));
        auctionProduct.setAuctionStatus(status);
        auctionProductRepository.save(auctionProduct);
    }

    //경매 입찰
    @Override
    public boolean placeBid(Long auctionProductId, User user, double bidAmount) {
        AuctionProduct auctionProduct = auctionProductRepository.findById(auctionProductId)
                .orElseThrow(() -> new RuntimeException("Auction product not found"));

        boolean isBidPlaced = auctionProduct.placeBid(user, bidAmount);
        auctionProductRepository.save(auctionProduct);

        return isBidPlaced;
    }

    //경매 입찰 취소
    @Override
    public boolean cancelBid(Long bidId) {
        AuctionBid auctionBid = auctionBidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        auctionBidRepository.delete(auctionBid);
        return true;
    }

    //경매 상품 상태 확인
    @Scheduled(cron = "0 * * * * ?") // 매 분마다 실행
    public void checkAndEndAuctions() {
        List<AuctionProduct> ongoingAuctions = auctionProductRepository.findByAuctionProductStatus(AuctionProductStatus.BID);

        for (AuctionProduct auctionProduct : ongoingAuctions) {
            if (auctionProduct.isAuctionEnded()) {
                auctionProduct.endAuction();
                auctionProductRepository.save(auctionProduct);
            }
        }
    }

    //입금 대기 상품
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAuctionStatus() {
        List<AuctionProduct> auctionProducts = auctionProductRepository.findByAuctionProductStatus(AuctionProductStatus.PENDING);

        for (AuctionProduct auctionProduct : auctionProducts) {
            if (auctionProduct.getUpdatedAt().plusDays(1).isBefore(LocalDateTime.now()) && !isPaymentConfirmed(auctionProduct)) {
                auctionProduct.selectNextHighestBid();
                auctionProductRepository.save(auctionProduct);
            }
        }
    }

    //경매 상태 SOLD인지 Check
    private boolean isPaymentConfirmed(AuctionProduct auctionProduct) {
        return auctionProduct.getAuctionProductStatus() == AuctionProductStatus.SOLD;
    }

    //경매 상태 SOLD 전환
    public boolean confirmPayment(Long productId) {
        AuctionProduct auctionProduct = auctionProductRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Auction product not found"));

        if (auctionProduct.getAuctionProductStatus() == AuctionProductStatus.SOLD) {
            return false;
        }

        auctionProduct.setAuctionStatus(AuctionProductStatus.SOLD);
        auctionProductRepository.save(auctionProduct);

        return true;
    }
}
