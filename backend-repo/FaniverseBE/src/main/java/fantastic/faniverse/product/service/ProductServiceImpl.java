package fantastic.faniverse.product.service;
import fantastic.faniverse.Exception.ProductNotFoundException;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.AuctionProduct.repository.AuctionProductRepository;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.GeneralProduct.repository.GeneralProductRepository;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.ProductCategory.domain.ProductCategory;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import fantastic.faniverse.product.dto.ProductDto;
import fantastic.faniverse.product.dto.ProductListResponse;
import fantastic.faniverse.product.repository.ProductRepository;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Getter
@Transactional
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final GeneralProductRepository generalProductRepository;
    private final AuctionProductRepository auctionProductRepository;

    //전체 상품 반환
    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    //전체상품 중 상품 1개 찾기
    @Override
    public Product findOne(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));
    }

    //상품 삭제
    @Override
    public void deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        } else {
            throw new ProductNotFoundException("상품을 찾을 수 없습니다.");
        }
    }

    //홈화면 - 최근 등록된 상품
    @Override
    public List<ProductListResponse> getRecentProducts() {
        return productRepository.findTop10ByOrderByCreatedAtDesc(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(ProductListResponse::new)
                .collect(Collectors.toList());
    }


    //카테고리별 상품
    @Override
    public List<ProductListResponse> getProductsByCategory(List<String> categories) {
        // 각 카테고리(총 11개)에서 상품 1개씩 반환
        // 각 카테고리에서 첫 번째 상품만 가져옴
        return categories.stream()
                .flatMap(categoryTitle -> {
                    // 자식 카테고리 가져오기
                    List<String> childCategories = ProductCategory.getChildCategories(categoryTitle);

                    // 자식 카테고리가 있는 경우 자식 카테고리에서 상품을 가져옴
                    return childCategories.stream()
                            .flatMap(childCategoryTitle -> productRepository.findByCategory(childCategoryTitle)
                                    .stream()
                                    .findFirst()  // 첫 번째 상품만 가져오기
                                    .map(ProductListResponse::new)
                                    .stream());
                })
                .collect(Collectors.toList());
    }
}