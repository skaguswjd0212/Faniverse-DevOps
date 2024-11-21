package fantastic.faniverse.product.AuctionProduct.repository;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface AuctionProductRepository extends JpaRepository<AuctionProduct, Long> {
    List<AuctionProduct> findByAuctionProductStatus(AuctionProductStatus status);
}
