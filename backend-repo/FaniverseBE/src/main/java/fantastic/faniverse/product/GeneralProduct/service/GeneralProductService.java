package fantastic.faniverse.product.GeneralProduct.service;

import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductRegisterRequest;
import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Transactional
@Service
public interface GeneralProductService {
    Long saveGeneralProduct(GeneralProductRegisterRequest form, Long userId) throws IOException;
    Long updateProduct(GeneralProductUpdateRequest request, Long productId, Long userId) throws IOException;
    void updateGeneralProductStatus(Long id, GeneralProductStatus status);
    GeneralProduct findOne(Long productId);
    String uploadImageToGCS(MultipartFile image) throws IOException;
}
