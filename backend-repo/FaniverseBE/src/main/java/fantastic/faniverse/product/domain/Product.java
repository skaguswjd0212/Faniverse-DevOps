package fantastic.faniverse.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fantastic.faniverse.chat.domain.ChatRoom;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.wishlist.entity.Wishlist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Product")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EntityListeners(AuditingEntityListener.class)
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "category")
    private String category;

    @Column(name = "image_url")
    private String imageUrl;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sellerId")
    private User seller;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Wishlist> wishlist = new ArrayList<>();

    // 상품 정보 변경
    public void update(Product product) {
        this.title = product.getTitle();
        this.content = product.getContent();
        this.updatedAt = product.getUpdatedAt();
        this.category = product.getCategory();
        this.imageUrl = product.getImageUrl();
    }

    public abstract ProductDetailsResponse toProductDetail();

    public abstract double getPrice();
}
