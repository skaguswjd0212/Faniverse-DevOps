package fantastic.faniverse.product.controller;

import fantastic.faniverse.chat.application.ChatRoomService;
import fantastic.faniverse.chat.domain.ChatRoom;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductRegisterRequest;
import fantastic.faniverse.product.GeneralProduct.service.GeneralProductServiceImpl;
import fantastic.faniverse.product.ProductCategory.domain.ProductCategory;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.AuctionProduct.service.AuctionProductServiceImpl;
import fantastic.faniverse.product.AuctionProduct.dto.AuctionProductRegisterRequest;
import fantastic.faniverse.product.ProductImage.ImageUploadRequest;
import fantastic.faniverse.product.ProductImage.ImageUploadService;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import fantastic.faniverse.product.dto.ProductListResponse;
import fantastic.faniverse.product.service.ProductServiceImpl;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import fantastic.faniverse.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    private final GeneralProductServiceImpl generalProductService;
    private final AuctionProductServiceImpl auctionProductService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;


    @Autowired
    private ImageUploadService imageUploadService;

    // 전체 상품 리스트 출력
    @GetMapping("/list")
    public ResponseEntity<List<ProductListResponse>> getProducts(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        // 상품 목록 조회
        List<ProductListResponse> products = productService.findAllProducts().stream()
                .map(ProductListResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    // 일반 상품 등록
    @PostMapping("/generalproducts/register")
    public ResponseEntity<String> registerGeneralProduct(
            HttpSession session,
            @RequestParam String title,
            @RequestParam String category,
            @RequestParam String content,
            @RequestParam double price,
            @RequestParam("image") MultipartFile image) throws IOException {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        User user = userService.findUserById(userId); // userId로 User 객체를 데이터베이스에서 조회

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        // 로그로 파일 이름 확인
        String imageName = image.getOriginalFilename();
        System.out.println("Received file name: " + imageName);

        // 파일 검증
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("Image file is required.");
        }

        if (imageName == null || imageName.isEmpty()) {
            return ResponseEntity.badRequest().body("Image file name is required.");
        }

        // 이미지 업로드 요청을 위한 설정
        ImageUploadRequest imageRequest = new ImageUploadRequest();
        imageRequest.setFile(image);
        imageRequest.setName(imageName);

        // 이미지 업로드
        String imageUrl;
        try {
            imageUrl = imageUploadService.uploadImage(imageRequest);
            System.out.println("Image uploaded successfully. URL: " + imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }

        // 일반 상품 등록 요청
        GeneralProductRegisterRequest form = GeneralProductRegisterRequest.builder()
                .title(title)
                .category(category)
                .content(content)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        try {
            Long productId = generalProductService.saveGeneralProduct(form, userId);
            return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/products/" + productId).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register product");
        }
    }


    // 경매 상품 등록
    @PostMapping(value = "/auctionproducts/register", consumes = "multipart/form-data")
    public ResponseEntity<String> registerAuctionProduct(
            HttpSession session,
            @RequestParam String title,
            @RequestParam String category,
            @RequestParam String content,
            @RequestParam double startingPrice,
            @RequestParam("image") MultipartFile image,
            @RequestParam String endDate) throws IOException {

        // 세션에서 userId 가져오기
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        // LocalDateTime 파라미터 변환
        LocalDateTime endDateTime;
        try {
            endDateTime = LocalDateTime.parse(endDate);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm:ss");
        }

        // 로그로 파일 이름 확인
        String imageName = image.getOriginalFilename();
        System.out.println("Received file name: " + imageName);

        // 파일 검증
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("Image file is required.");
        }

        if (imageName == null || imageName.isEmpty()) {
            return ResponseEntity.badRequest().body("Image file name is required.");
        }

        // 이미지 업로드 요청을 위한 설정
        ImageUploadRequest imageRequest = new ImageUploadRequest();
        imageRequest.setFile(image);
        imageRequest.setName(imageName);

        // 이미지 업로드
        String imageUrl;
        try {
            imageUrl = imageUploadService.uploadImage(imageRequest);
            System.out.println("Image uploaded successfully. URL: " + imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }

        // 경매 상품 등록 요청
        AuctionProductRegisterRequest form = AuctionProductRegisterRequest.builder()
                .title(title)
                .category(category)
                .content(content)
                .startingPrice(startingPrice)
                .imageUrl(imageUrl)
                .endDate(endDateTime)
                .build();

        try {
            Long productId = auctionProductService.saveAuctionProduct(form, userId);
            return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/products/" + productId).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register product");
        }
    }

    // 상품 상세 정보 조회
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductDetail(
            HttpSession session,
            @PathVariable Long productId) {

        // 세션에서 userId 가져오기
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }
        // 데이터베이스에서 User 객체 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 상품 조회
        Product product = productService.findOne(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        // ProductDetailsResponse 객체로 변환
        ProductDetailsResponse productDetail = product.toProductDetail();
        return ResponseEntity.ok(productDetail);
    }

    // 판매 상품 상태 변경
    @PutMapping("/{productId}/status")
    public ResponseEntity<Void> updateProductStatus(
            HttpSession session,
            @PathVariable Long productId,
            @RequestParam String status) {
        Long userId = (Long) session.getAttribute("userId");
        Product product = productService.findOne(productId);

        if (product instanceof GeneralProduct) {
            GeneralProductStatus generalStatus = GeneralProductStatus.valueOf(status);
            generalProductService.updateGeneralProductStatus(productId, generalStatus);
        } else {
            AuctionProductStatus auctionStatus = AuctionProductStatus.valueOf(status);
            auctionProductService.updateAuctionProductStatus(productId, auctionStatus);
        }
        return ResponseEntity.ok().build();
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            HttpSession session,
            @PathVariable Long productId) {
        Long userId = (Long) session.getAttribute("userId");
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // 홈화면 - 최근 등록된 상품 조회, 카테고리별 상품 조회
    @GetMapping("/home")
    public ResponseEntity<Map<String, List<ProductListResponse>>> getHomePage(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        // 자식 카테고리가 있는 경우 자식 카테고리만 포함, 부모 카테고리가 없는 경우 부모 카테고리만 포함
        List<String> categories = List.of(
                "걸그룹", "보이그룹", "솔로", "트로트", "기타",  // 음악 카테고리의 자식들
                "축구", "농구", "배구", "야구",  // 스포츠 카테고리의 자식들
                "애니", "게임"  // 자식이 없는 카테고리
        );

        List<ProductListResponse> recentProducts = productService.getRecentProducts(); // 최근 상품 10개
        List<ProductListResponse> firstItemInCategory = productService.getProductsByCategory(categories); // 카테고리별 상품 11개

        Map<String, List<ProductListResponse>> response = Map.of(
                "recentProducts", recentProducts,
                "firstItemInCategory", firstItemInCategory
        );

        return ResponseEntity.ok(response);
    }
}