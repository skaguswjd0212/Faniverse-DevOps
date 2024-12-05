package fantastic.faniverse.product.GeneralProduct.domain;

import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "GeneralProduct")
@Entity
@DiscriminatorValue("general_product")
public class GeneralProduct extends Product {

    @Column(name = "price")
    private double price;

    //상품 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "generalProductStatus")
    private GeneralProductStatus generalProductStatus;

    @Override
    public void update(Product product) {
        super.update(product);
        if (product instanceof GeneralProduct) {
            this.price = ((GeneralProduct) product).getPrice();
            this.generalProductStatus = ((GeneralProduct) product).getGeneralProductStatus();
        }
    }

    @Override
    public ProductDetailsResponse toProductDetail() {
        return new ProductDetailsResponse(this);
    }

    @Override
    public double getPrice() {
        return price;
    }

    // 상품 상태 지정
    public void setStatus(GeneralProductStatus status) {
        this.generalProductStatus = status;
    }
}
