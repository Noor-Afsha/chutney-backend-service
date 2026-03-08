package in.co.application.chutneyApplication.repository;

import in.co.application.chutneyApplication.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
