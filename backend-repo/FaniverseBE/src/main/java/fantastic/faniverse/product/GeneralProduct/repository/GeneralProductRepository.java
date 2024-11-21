package fantastic.faniverse.product.GeneralProduct.repository;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralProductRepository extends JpaRepository<GeneralProduct, Long> {
}
