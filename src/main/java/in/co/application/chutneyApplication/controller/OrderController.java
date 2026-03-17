package in.co.application.chutneyApplication.controller;

import in.co.application.chutneyApplication.commons.ApiResponse;
import in.co.application.chutneyApplication.dto.OrderRequestDto;
import in.co.application.chutneyApplication.entity.Orders;
import in.co.application.chutneyApplication.repository.OrderRepository;
import in.co.application.chutneyApplication.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<Orders> placeOrder(@Valid @RequestBody OrderRequestDto dto) throws Throwable {
        Orders order = orderService.placeOrder(dto);
        return new ApiResponse<>(200, true, "Order created", order);
    }

    @PostMapping("/verify")
    public ApiResponse<String> verifyPayment(@RequestBody Map<String, String> data) throws Exception {

        boolean verified = orderService.verifyPayment(
                data.get("razorpay_order_id"),
                data.get("razorpay_payment_id"),
                data.get("razorpay_signature"));

        if (verified) {
            return new ApiResponse<>(200, true, "Payment Successful", null);
        }
        return new ApiResponse<>(400, false, "Payment Failed", null);
    }

    @GetMapping("/{phone}")
    public List<Orders> getOrdersByPhone(@PathVariable String phone) {
        return orderService.getOrdersByPhone(phone);
    }

    @PutMapping("/{orderId}/cancel")
    public ApiResponse<String> cancelOrder(@PathVariable Long orderId) {

        orderService.cancelOrder(orderId);

        return new ApiResponse<>(200, true, "Order Cancelled", null);
    }
    @GetMapping("/{orderId}/invoice")
    public ApiResponse<Orders> getInvoice(@PathVariable Long orderId) {

        Orders order = orderService.getOrderById(orderId);

        return new ApiResponse<>(200, true, "Invoice fetched", order);
    }
    @GetMapping("/{orderId}/track")
    public ApiResponse<Orders> trackOrder(@PathVariable Long orderId) {

        Orders order = orderService.getOrderById(orderId);

        return new ApiResponse<>(200, true, "Tracking info", order);
    }
}