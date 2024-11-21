package fantastic.faniverse.product.ProductCategory.controller;

import fantastic.faniverse.product.ProductCategory.dto.ProductCategoryDto;
import fantastic.faniverse.product.ProductCategory.domain.ProductCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class ProductCategoryController {

    // 최상위 카테고리 반환 (음악, 스포츠, 애니, 게임)
    @GetMapping("/root")
    public ResponseEntity<List<String>> getRootCategories() {
        List<String> rootCategories = ProductCategory.getRootCategories();  // 최상위 카테고리 가져오기
        return ResponseEntity.ok(rootCategories);
    }

    // 특정 상위 카테고리의 하위 카테고리 반환
    @GetMapping("/subcategories/{parentTitle}")
    public ResponseEntity<List<String>> getSubCategories(@PathVariable String parentTitle) {
        List<String> subCategories = ProductCategory.getChildCategories(parentTitle);  // 자식 카테고리 가져오기
        return ResponseEntity.ok(subCategories);
    }
}
