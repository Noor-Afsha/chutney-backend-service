package in.co.application.chutneyApplication.controller;

import in.co.application.chutneyApplication.entity.OrderStatusHistory;
import in.co.application.chutneyApplication.service.OrderTrackingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderTrackingController {

    private final OrderTrackingService orderTrackingService;

    public OrderTrackingController(OrderTrackingService orderTrackingService) {
        this.orderTrackingService = orderTrackingService;
    }

    @GetMapping("/orders/{id}/tracking")
    public List<OrderStatusHistory> getOrderTracking(@PathVariable Long id){

        return orderTrackingService.getOrderTracking(id);

    }
}