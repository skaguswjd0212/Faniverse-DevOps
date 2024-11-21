package fantastic.faniverse.product.AuctionProduct.controller;

import fantastic.faniverse.product.AuctionProduct.service.AuctionProductService;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
public class AuctionProductController {
    private final AuctionProductService auctionProductService;
    private final UserRepository userRepository;

    // 사용자 - 경매 입찰 등록
    @PostMapping("/{productId}/bids")
    public ResponseEntity<String> placeBid(@PathVariable Long productId, @RequestBody Map<String, Object> request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        Double bidAmount = Double.parseDouble(request.get("bidAmount").toString());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isBidPlaced = auctionProductService.placeBid(productId, user, bidAmount);
        if (isBidPlaced) {
            return ResponseEntity.ok("Bid placed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to place bid.");
        }
    }

    // 사용자 - 경매 입찰 취소
    @DeleteMapping("/bids/{bidId}")
    public ResponseEntity<String> cancelBid(@PathVariable Long bidId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        boolean isBidCancelled = auctionProductService.cancelBid(bidId);
        if (isBidCancelled) {
            return ResponseEntity.ok("Bid cancelled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel bid.");
        }
    }

    // 경매 금액 지불 여부 확인 (판매자 수동)
    @PutMapping("/{productId}/confirm-payment")
    public ResponseEntity<String> confirmPayment(@PathVariable Long productId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        boolean isPaymentConfirmed = auctionProductService.confirmPayment(productId);
        if (isPaymentConfirmed) {
            return ResponseEntity.ok("Payment confirmed.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to confirm payment.");
        }
    }
}
