//package in.co.application.chutneyApplication.controller;
//
//import com.razorpay.RazorpayException;
//import in.co.application.chutneyApplication.service.RazorpayService;
//import com.razorpay.Order;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/payment")
//@CrossOrigin("*")
//public class PaymentController {
//
//    @Autowired
//    private RazorpayService razorpayService;
//
//    @PostMapping("/create-order")
//    public Map<String, Object> createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
//        int amount = (int) data.get("amount"); // amount in rupees
//        String currency = (String) data.getOrDefault("currency", "INR");
//        String receipt = (String) data.getOrDefault("receipt", "receipt#1");
//
//        Order order = (Order) razorpayService.createOrder(amount, currency, receipt);
//
//        return Map.of(
//                "id", order.get("id"),
//                "amount", order.get("amount"),
//                "currency", order.get("currency")
//        );
//    }
//}