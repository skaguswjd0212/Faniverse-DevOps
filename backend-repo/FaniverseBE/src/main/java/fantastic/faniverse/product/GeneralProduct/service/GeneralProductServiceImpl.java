package fantastic.faniverse.product.GeneralProduct.service;

import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductUpdateRequest;
import fantastic.faniverse.product.GeneralProduct.repository.GeneralProductRepository;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductRegisterRequest;
import fantastic.faniverse.product.ProductImage.ImageUploadRequest;
import fantastic.faniverse.Exception.ProductNotFoundException;
import fantastic.faniverse.product.ProductImage.ImageUploadService;
import fantastic.faniverse.product.service.ProductService;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Transactional
public class GeneralProductServiceImpl implements GeneralProductService {

    private final UserRepository userRepository;
    private final GeneralProductRepository generalProductRepository;
    private final ImageUploadService imageUploadService;
    private final ProductService productService;

    @Override
    public Long saveGeneralProduct(GeneralProductRegisterRequest request, Long userId) throws IOException {
        // 사용자 검증
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new ProductNotFoundException("사용자를 찾을 수 없습니다"));

        // 이미지 업로드 처리 (이미 컨트롤러에서 처리됨)
       String imageUrl = request.getImageUrl();

        // GeneralProduct 엔티티 생성
        GeneralProduct generalProduct = request.toGeneralProductEntity(imageUrl);

        System.out.println("Image URL before save: " + generalProduct.getImageUrl());

        generalProduct.setSeller(seller);  // seller 설정

        // 상품 저장
        generalProductRepository.save(generalProduct);

        return generalProduct.getId(); // 반환할 값 확인
    }

    @Override
    public Long updateProduct(GeneralProductUpdateRequest request, Long productId, Long userId) throws IOException {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new ProductNotFoundException("사용자를 찾을 수 없습니다"));

        // 기존 상품 정보를 조회
        GeneralProduct existingProduct = generalProductRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));

        // 이미지 업데이트 처리
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            existingProduct.setImageUrl(request.getImageUrl());
        }
        // 상품 정보 업데이트
        request.updateEntity(existingProduct);
        existingProduct.setSeller(seller); // seller 설정

        // 상품 저장
        generalProductRepository.save(existingProduct);

        return existingProduct.getId();
    }


    @Override
    public void updateGeneralProductStatus(Long id, GeneralProductStatus newStatus) {
        GeneralProduct product = generalProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));

        // 상태 변경에 따른 검증 로직 추가
        GeneralProductStatus currentStatus = product.getGeneralProductStatus();

        if (newStatus == GeneralProductStatus.COMPLETED) {
            if (currentStatus != GeneralProductStatus.RESERVATION) {
                throw new IllegalStateException("상품이 예약된 상태가 아니면 판매 완료로 변경할 수 없습니다.");
            }
            completeProductSale(product);
        } else if (newStatus == GeneralProductStatus.RESERVATION) {
            if (currentStatus != GeneralProductStatus.SALE) {
                throw new IllegalStateException("상품이 판매중인 상태에서만 예약 상태로 변경할 수 있습니다.");
            }
            reserveProduct(product);
        } else if (newStatus == GeneralProductStatus.SALE) {
            if (currentStatus == GeneralProductStatus.COMPLETED) {
                throw new IllegalStateException("판매 완료된 상품은 다시 판매중 상태로 변경할 수 없습니다.");
            }
            product.setStatus(GeneralProductStatus.SALE);
        }

        // 상태 변경 저장
        product.setStatus(newStatus);
        generalProductRepository.save(product);
    }

    private void completeProductSale(GeneralProduct product) {
        // 판매 완료 처리 로직 (예: 판매 기록 저장, 알림 발송 등)
        System.out.println("Product " + product.getId() + " has been marked as sold.");
    }

    private void reserveProduct(GeneralProduct product) {
        // 예약 처리 로직 (예: 예약 알림 발송 등)
        System.out.println("Product " + product.getId() + " has been reserved.");
    }

    @Override
    public GeneralProduct findOne(Long productId) {
        return generalProductRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));
    }

    @Override
    public String uploadImageToGCS(MultipartFile image) throws IOException {
        // 이미지 업로드 로직 구현
        ImageUploadRequest imageUploadRequest = new ImageUploadRequest();
        imageUploadRequest.setFile(image);
        return imageUploadService.uploadImage(imageUploadRequest);
    }
}
