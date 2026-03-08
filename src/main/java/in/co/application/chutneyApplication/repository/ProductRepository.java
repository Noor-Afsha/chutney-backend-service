package in.co.application.chutneyApplication.repository;

import in.co.application.chutneyApplication.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}