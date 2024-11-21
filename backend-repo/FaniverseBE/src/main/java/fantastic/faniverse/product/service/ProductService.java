package fantastic.faniverse.product.service;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.dto.ProductDto;
import fantastic.faniverse.product.dto.ProductListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public interface ProductService {
    void deleteProduct(Long productId);
    List<ProductListResponse> getRecentProducts();
    List<ProductListResponse> getProductsByCategory(List<String> categories);
    Product findOne(Long id);
    List<Product> findAllProducts();
}
