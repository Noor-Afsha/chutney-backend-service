package in.co.application.chutneyApplication.service;

import in.co.application.chutneyApplication.entity.OrderStatusHistory;
import in.co.application.chutneyApplication.repository.OrderStatusHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTrackingService {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    public OrderTrackingService(OrderStatusHistoryRepository orderStatusHistoryRepository) {
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
    }

    public List<OrderStatusHistory> getOrderTracking(Long orderId){

        return orderStatusHistoryRepository
                .findByOrderIdOrderByUpdatedAtAsc(orderId);
    }

}