package fantastic.faniverse.wishlist.controller;

import fantastic.faniverse.wishlist.dto.WishlistProductDto;
import fantastic.faniverse.wishlist.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // 사용자의 wishlist 조회
    @GetMapping("/user")
    public ResponseEntity<List<WishlistProductDto>> getWishlistProducts(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            List<WishlistProductDto> wishlist = wishlistService.getWishlistByUserId(userId); // WishlistProductDto로 변경
            return ResponseEntity.ok(wishlist);
        } else {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
    }


    // wishlist 항목 추가
    @PostMapping("/add")
    public ResponseEntity<String> addWishlistItem(@RequestParam Long productId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            wishlistService.addWishlistItem(userId, productId);
            return ResponseEntity.ok("Item added to wishlist");
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

    // wishlist 항목 삭제
    @DeleteMapping("/remove/{wishlistId}")
    public ResponseEntity<String> removeWishlistItem(@PathVariable Long wishlistId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            wishlistService.removeWishlistItem(userId, wishlistId);
            return ResponseEntity.ok("Item removed from wishlist");
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }
}
