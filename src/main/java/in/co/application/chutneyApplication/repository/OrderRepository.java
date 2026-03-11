package in.co.application.chutneyApplication.repository;
import in.co.application.chutneyApplication.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findByRazorpayOrderId(String razorpayOrderId);
    List<Orders> findByPhoneOrderByIdDesc(String phone);
    List<Orders> findByPhoneOrderByCreatedAtDesc(String phone);


}