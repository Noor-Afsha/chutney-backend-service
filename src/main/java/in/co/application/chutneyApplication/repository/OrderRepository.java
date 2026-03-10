package in.co.application.chutneyApplication.repository;
import in.co.application.chutneyApplication.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findByRazorpayOrderId(String razorpayOrderId);
}