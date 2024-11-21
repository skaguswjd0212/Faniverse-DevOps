package fantastic.faniverse.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WishlistProductDto {
    private Long wishlistId;
    private Long userId;
    private Long productId;
    private String title;
    private String content;
    private String category;
    private String imageUrl;
    private Double price;
}
