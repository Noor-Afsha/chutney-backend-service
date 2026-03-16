package in.co.application.chutneyApplication.service;

import in.co.application.chutneyApplication.commons.OrderStatus;
import in.co.application.chutneyApplication.entity.Orders;
import in.co.application.chutneyApplication.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final OrderRepository orderRepository;
    private final FirebaseOrderService firebaseOrderService;

    public AdminService(OrderRepository orderRepository, FirebaseOrderService firebaseOrderService) {
        this.orderRepository = orderRepository;
        this.firebaseOrderService = firebaseOrderService;
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    // ADMIN - UPDATE ORDER STATUS
    public Orders updateOrderStatus(Long id, String status) {

        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.valueOf(status));

        Orders updatedOrder = orderRepository.save(order);

        //PUSH UPDATE TO FIREBASE
        firebaseOrderService.sendOrderToFirebase(updatedOrder);

        return updatedOrder;
    }
}