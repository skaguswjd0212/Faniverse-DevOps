package fantastic.faniverse.keyword.controller;

import fantastic.faniverse.keyword.dto.KeywordDto;
import fantastic.faniverse.keyword.dto.KeywordProductDto;
import fantastic.faniverse.keyword.entity.Keyword;
import fantastic.faniverse.keyword.service.KeywordService;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.dto.ProductDto;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/keywords")
public class KeywordController {

    @Autowired
    private KeywordService keywordService;

    // 키워드 추가
    @PostMapping("/add")
    public ResponseEntity<Keyword> addKeyword(@RequestBody KeywordDto keywordDto, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Keyword keyword = keywordService.addKeyword(keywordDto, userId);
            return ResponseEntity.ok(keyword);
        } else {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
    }

    // 키워드 수정
    @PutMapping("/{id}")
    public ResponseEntity<Keyword> updateKeyword(@PathVariable Long id,
                                                 @RequestBody KeywordDto keywordDto,
                                                 HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Keyword keyword = keywordService.updateKeyword(id, keywordDto, userId);
            if (keyword != null) {
                return ResponseEntity.ok(keyword);
            }
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
    }

    // 키워드 삭제
    @DeleteMapping("/remove")
    public ResponseEntity<String> deleteKeyword(@RequestParam Long keywordId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            String response = keywordService.deleteKeyword(keywordId, userId);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

    // 사용자의 키워드 목록 조회
    @GetMapping("/user")
    public ResponseEntity<List<KeywordDto>> getUserKeywords(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            List<KeywordDto> keywords = keywordService.findKeywordsByUserId(userId);
            return ResponseEntity.ok(keywords);
        } else {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
    }

    // 단어를 title에 포함하고 있는 상품 목록 조회
    @GetMapping("/")
    public ResponseEntity<List<KeywordProductDto>> getProductsByKeyword(@RequestParam String keyword) {
        List<KeywordProductDto> keywordProductDtos = keywordService.findProductsByKeyword(keyword);
        return ResponseEntity.ok(keywordProductDtos);
    }
}
