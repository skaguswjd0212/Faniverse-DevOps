package fantastic.faniverse.product.GeneralProduct.controller;

import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductUpdateRequest;
import fantastic.faniverse.product.GeneralProduct.service.GeneralProductServiceImpl;
import fantastic.faniverse.product.ProductImage.ImageUploadRequest;
import fantastic.faniverse.product.ProductImage.ImageUploadService;
import fantastic.faniverse.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class GeneralProductController {

    private final ImageUploadService imageUploadService;
    private final GeneralProductServiceImpl generalProductService;

    // 상품 수정
    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,
                                                HttpSession session,
                                                @RequestParam String title,
                                                @RequestParam String category,
                                                @RequestParam String content,
                                                @RequestParam double price,
                                                @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        // 세션에서 사용자 정보를 가져옴
        //User user = (User) session.getAttribute("user");
        Long userId = (Long) session.getAttribute("userId");

        // 기존의 상품 정보를 불러옴
        GeneralProduct existingProduct = generalProductService.findOne(productId);
        String imageUrl = existingProduct.getImageUrl();

        if (image != null && !image.isEmpty()) {
            // 로그로 파일 이름 확인
            String imageName = image.getOriginalFilename();
            System.out.println("Received file name: " + imageName);

            // 파일 검증
            if (imageName == null || imageName.isEmpty()) {
                return ResponseEntity.badRequest().body("Image file name is required.");
            }

            // 이미지 업로드 요청을 위한 설정
            ImageUploadRequest imageRequest = new ImageUploadRequest();
            imageRequest.setFile(image);
            imageRequest.setName(imageName);

            // 이미지 업로드
            try {
                imageUrl = imageUploadService.uploadImage(imageRequest);
                System.out.println("Image uploaded successfully. URL: " + imageUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
            }
        }

        // 상품 업데이트 요청 데이터 생성
        GeneralProductUpdateRequest form = GeneralProductUpdateRequest.builder()
                .title(title)
                .category(category)
                .content(content)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        // 업데이트 로직 실행
        generalProductService.updateProduct(form, productId, userId);

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/products/" + productId).build();
    }
}
