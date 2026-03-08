package in.co.application.chutneyApplication.repository;
import in.co.application.chutneyApplication.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByProductId(Long productId);
}