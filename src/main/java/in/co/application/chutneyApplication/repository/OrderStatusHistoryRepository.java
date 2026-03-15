package in.co.application.chutneyApplication.repository;

import in.co.application.chutneyApplication.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {

    List<OrderStatusHistory> findByOrderIdOrderByUpdatedAtAsc(Long orderId);

}