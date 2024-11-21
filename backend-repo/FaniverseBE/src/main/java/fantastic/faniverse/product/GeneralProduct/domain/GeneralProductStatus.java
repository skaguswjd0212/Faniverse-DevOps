package fantastic.faniverse.product.GeneralProduct.domain;
import lombok.Getter;

@Getter
public enum GeneralProductStatus {
        SALE("판매중"),
        COMPLETED("판매완료"),
        RESERVATION("예약중");

        private String value;

        GeneralProductStatus(String value) {
                this.value = value;
        }
}
