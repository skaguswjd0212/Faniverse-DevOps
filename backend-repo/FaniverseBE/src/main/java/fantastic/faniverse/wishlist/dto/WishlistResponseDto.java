package fantastic.faniverse.wishlist.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WishlistResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private Long userId;
    private WishlistProductDto product;

    public WishlistResponseDto(Long id, LocalDateTime createdAt, Long userId, WishlistProductDto product) {
        this.id = id;
        this.createdAt = createdAt;
        this.userId = userId;
        this.product = product;
    }
}
