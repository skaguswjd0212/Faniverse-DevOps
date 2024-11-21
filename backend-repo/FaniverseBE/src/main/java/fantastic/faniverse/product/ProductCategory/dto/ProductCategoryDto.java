package fantastic.faniverse.product.ProductCategory.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductCategoryDto {
    private final String title;
    private final String parentTitle;
}
