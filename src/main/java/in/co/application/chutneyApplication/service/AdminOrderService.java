package in.co.application.chutneyApplication.service;

import in.co.application.chutneyApplication.commons.OrderStatus;
import in.co.application.chutneyApplication.entity.OrderStatusHistory;
import in.co.application.chutneyApplication.entity.Orders;
import in.co.application.chutneyApplication.repository.OrderRepository;
import in.co.application.chutneyApplication.repository.OrderStatusHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository orderStatusHistory;

    public AdminOrderService(OrderRepository orderRepository, OrderStatusHistoryRepository orderStatusHistory) {
        this.orderRepository = orderRepository;
        this.orderStatusHistory = orderStatusHistory;
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Orders updateOrderStatus(Long id, String status) {

        Orders order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus newStatus = OrderStatus.valueOf(status);
        order.setStatus(newStatus);
        orderRepository.save(order);
        OrderStatusHistory history = new OrderStatusHistory(order.getId(), newStatus);
        orderStatusHistory.save(history);

        return order;
    }

    public void deleteOrder(Long id) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }
}