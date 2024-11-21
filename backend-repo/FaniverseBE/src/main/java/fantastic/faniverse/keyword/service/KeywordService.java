package fantastic.faniverse.keyword.service;

import fantastic.faniverse.keyword.dto.KeywordDto;
import fantastic.faniverse.keyword.dto.KeywordProductDto;
import fantastic.faniverse.keyword.entity.Keyword;
import fantastic.faniverse.keyword.repository.KeywordRepository;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.dto.GeneralProductDetailResponse;
import fantastic.faniverse.product.dto.ProductDto;
import fantastic.faniverse.product.repository.ProductRepository;
import fantastic.faniverse.product.service.ProductService;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import fantastic.faniverse.wishlist.entity.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    // 키워드 추가
    public Keyword addKeyword(KeywordDto keywordDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Keyword keyword = new Keyword();
        keyword.setWord(keywordDto.getWord());
        keyword.setUser(user);
        return keywordRepository.save(keyword);
    }

    // 키워드 수정
    public Keyword updateKeyword(Long id, KeywordDto keywordDto, Long userId) {
        Keyword keyword = keywordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Keyword not found"));

        if (!keyword.getUser().getId().equals(userId)) {
            throw new RuntimeException("User does not own this keyword");
        }

        keyword.setWord(keywordDto.getWord());
        keyword.setUpdatedAt(LocalDateTime.now());
        return keywordRepository.save(keyword);
    }

    // 유저의 키워드 조회
    public List<KeywordDto> findKeywordsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return keywordRepository.findByUser(user).stream()
                .map(keyword -> new KeywordDto(keyword.getId(), keyword.getWord(), userId))
                .collect(Collectors.toList());
    }

    // 키워드 삭제
    public String deleteKeyword(Long keywordId, Long userId) {
        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new RuntimeException("Keyword not found"));

        if (!keyword.getUser().getId().equals(userId)) {
            throw new RuntimeException("User does not own this keyword");
        }

        keywordRepository.deleteById(keywordId);
        return "Keyword deleted successfully";
    }






    // 키워드로 상품 찾기
   public List<KeywordProductDto> findProductsByKeyword(String keyword) {
        return productRepository.findProductsByKeyword(keyword);
   }
}
