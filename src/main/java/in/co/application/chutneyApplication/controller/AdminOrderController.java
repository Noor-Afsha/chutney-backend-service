package in.co.application.chutneyApplication.controller;

import in.co.application.chutneyApplication.commons.ApiResponse;
import in.co.application.chutneyApplication.entity.Orders;
import in.co.application.chutneyApplication.service.AdminOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    public AdminOrderController(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    @GetMapping
    public ApiResponse<List<Orders>> getAllOrders() {
        List<Orders> orders = adminOrderService.getAllOrders();
        return new ApiResponse<>(200, true, "Orders fetched successfully", orders);
    }

    @GetMapping("/{id}")
    public ApiResponse<Orders> getOrderById(@PathVariable Long id) {
        Orders order = adminOrderService.getOrderById(id);
        return new ApiResponse<>(200, true, "Order fetched successfully", order);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Orders> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Orders order = adminOrderService.updateOrderStatus(id, body.get("status"));
        return new ApiResponse<>(200, true, "Order status updated successfully", order);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteOrder(@PathVariable Long id) {
        adminOrderService.deleteOrder(id);
        return new ApiResponse<>(200, true, "Order deleted successfully", null);
    }
}