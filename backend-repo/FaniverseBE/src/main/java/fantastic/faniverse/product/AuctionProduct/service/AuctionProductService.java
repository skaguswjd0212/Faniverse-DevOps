package fantastic.faniverse.product.AuctionProduct.service;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.AuctionProduct.dto.AuctionProductRegisterRequest;
import fantastic.faniverse.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Transactional
@Service
public interface AuctionProductService {
    Long saveAuctionProduct(AuctionProductRegisterRequest request, Long userId) throws IOException;
    void updateAuctionProductStatus(Long id, AuctionProductStatus status);
    boolean placeBid(Long auctionProductId, User userId, double bidAmount);
    boolean cancelBid(Long bidId);
    boolean confirmPayment(Long productId);
}