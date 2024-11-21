package fantastic.faniverse.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDto {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;
}
