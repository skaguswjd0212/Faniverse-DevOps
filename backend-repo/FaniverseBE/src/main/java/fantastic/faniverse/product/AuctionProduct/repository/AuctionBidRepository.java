package fantastic.faniverse.product.AuctionProduct.repository;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionBidRepository extends JpaRepository<AuctionBid, Long> {
}
