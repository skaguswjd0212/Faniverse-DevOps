package fantastic.faniverse.wishlist.service;

import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.dto.GeneralProductDetailResponse;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import fantastic.faniverse.wishlist.dto.WishlistDto;
import fantastic.faniverse.wishlist.dto.WishlistProductDto;
import fantastic.faniverse.wishlist.entity.Wishlist;
import fantastic.faniverse.wishlist.repository.WishlistRepository;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import fantastic.faniverse.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;




    public List<WishlistProductDto> getWishlistByUserId(Long userId) {
        List<Wishlist> wishlistItems = wishlistRepository.findByUserId(userId);
        List<WishlistProductDto> wishlistProductDtos = new ArrayList<>();

        for (Wishlist item : wishlistItems) {
            Product product = item.getProduct(); // Wishlist에서 Product 객체를 가져옴
            if (product != null) {
                // GeneralProductDetailResponse 생성
                GeneralProductDetailResponse generalProductDetails = new GeneralProductDetailResponse(product);

                // WishlistProductDto 생성
                WishlistProductDto dto = new WishlistProductDto(
                        item.getId(), // wishlistId
                        item.getUser().getId(), // userId를 User 객체에서 가져옴
                        product.getId(),        // productId를 Product 객체에서 가져옴
                        product.getTitle(),     // Product의 제목
                        product.getContent(),   // Product의 내용
                        product.getCategory(),  // Product의 카테고리
                        product.getImageUrl(),   // Product의 이미지 URL
                        generalProductDetails.getPrice() // 가격
                );

                wishlistProductDtos.add(dto);
            }
        }

        return wishlistProductDtos;
    }




    public void addWishlistItem(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlistRepository.save(wishlist);
    }

    public void removeWishlistItem(Long userId, Long wishlistId) {
        // User와 Wishlist를 확인하기 위해 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                        .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        // wishlist user 맞는지 확인
        if (!wishlist.getUser().equals(user)) {
            throw new RuntimeException("Wishlist item does not belong to the user");
        }

        // wishlist 삭제
        wishlistRepository.deleteById(wishlistId);
    }
}
