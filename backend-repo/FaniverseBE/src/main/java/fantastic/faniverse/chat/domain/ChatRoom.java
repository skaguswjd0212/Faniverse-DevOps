package fantastic.faniverse.chat.domain;

import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "buyer_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;


    public static ChatRoom of(Long roomId) {
        return ChatRoom.builder()
                .id(roomId)
                .build();
    }
    public static ChatRoom create(User seller, User buyer, Product product) {
        return ChatRoom.builder()
                .user(seller)
                .buyer(buyer)
                .product(product)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .roomStatus(RoomStatus.NORMAL)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void updateBlock() {
        this.roomStatus = RoomStatus.BLOCKED;
    }

    public boolean isBlocked() {
        return this.roomStatus == RoomStatus.BLOCKED;
    }
}
