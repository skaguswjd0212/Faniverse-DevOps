package fantastic.faniverse.search.service;

import fantastic.faniverse.product.dto.ProductDetailsResponse;
import fantastic.faniverse.product.repository.ProductRepository;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    @Autowired
    private ProductRepository productRepository;
    private final UserRepository userRepository;

    // 최근 검색어 저장 (최대 5개)
    public void addRecentSearchToUser(Long userId, String searchWord) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.addRecentSearch(searchWord);
        userRepository.save(user);
    }

    //최근 검색어 가져오기
    public List<String> getRecentSearchesFromUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getRecentSearches();
    }

    // 검색어로 상품 검색
    public List<ProductDetailsResponse> searchProducts(Long userId, String searchWord) {
        addRecentSearchToUser(userId, searchWord);

        return productRepository.findByTitleContaining(searchWord)
                .stream()
                .map(Product::toProductDetail)
                .collect(Collectors.toList());
    }

    //상품 정렬 (고가, 저가, 최신순)
    public List<ProductDetailsResponse> filterProducts(String searchWord, String sortBy, Long userId) {
        // 검색어에 맞는 상품을 검색
        List<ProductDetailsResponse> filteredProducts = searchProducts(userId, searchWord);

        // 정렬 조건에 따른 상품 정렬
        switch (sortBy) {
            case "price_high":
                filteredProducts.sort(Comparator.comparingDouble(ProductDetailsResponse::getPrice).reversed());
                break;
            case "price_low":
                filteredProducts.sort(Comparator.comparingDouble(ProductDetailsResponse::getPrice));
                break;
            case "latest":
                filteredProducts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
                break;
            default:
                break;
        }
        return filteredProducts;
    }
}