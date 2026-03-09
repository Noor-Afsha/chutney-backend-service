package in.co.application.chutneyApplication.controller;
import in.co.application.chutneyApplication.commons.ApiResponse;
import in.co.application.chutneyApplication.dto.OrderRequestDto;
import in.co.application.chutneyApplication.entity.Orders;
import in.co.application.chutneyApplication.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<Orders> placeOrder(@Valid @RequestBody OrderRequestDto dto) {
        Orders order = orderService.placeOrder(dto);
        return new ApiResponse<>(200, true, "Order placed successfully", order
        );
    }

    @PostMapping("/{orderId}/pay")
    public ApiResponse<String> payOrder(@PathVariable Long orderId) {
        Orders order = orderService.getOrderById(orderId);
        if(order == null) {
            return new ApiResponse<>(404, false, "Order not found", null);
        }

        // Simulate scanner payment success
        order.setStatus("PAID");
        orderService.updateOrder(order);

        // Generate dummy receipt
        String receipt = "Receipt\nOrder ID: " + order.getId() +
                "\nName: " + order.getName() +
                "\nAmount: " + order.getTotalAmount() +
                "\nStatus: " + order.getStatus();

        return new ApiResponse<>(200, true, "Payment successful", receipt);
    }
}