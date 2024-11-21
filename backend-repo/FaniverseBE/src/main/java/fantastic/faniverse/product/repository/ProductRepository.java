package fantastic.faniverse.product.repository;

import fantastic.faniverse.keyword.dto.KeywordProductDto;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.dto.ProductDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>  {
    List<Product> findByTitleContainingOrContentContaining(String title, String content);
    List<Product> findBySellerId(Long userId);
    List<Product> findTop10ByOrderByCreatedAtDesc(Sort createdAt);
    List<Product> findByTitleContaining(String searchKeyword);
    List<Product> findByCategory(String category);


    // 키워드를 통해 상품을 검색하는 메서드
    @Query("SELECT new fantastic.faniverse.keyword.dto.KeywordProductDto(gp.id, :keyword, gp.title, gp.content, gp.category, gp.imageUrl, gp.price) " +
            "FROM GeneralProduct gp " +
            "WHERE gp.title LIKE %:keyword%")
    List<KeywordProductDto> findProductsByKeyword(@Param("keyword") String keyword);


}
