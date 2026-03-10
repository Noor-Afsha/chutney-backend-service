package in.co.application.chutneyApplication.service;

import com.razorpay.Utils;
import in.co.application.chutneyApplication.dto.OrderRequestDto;
import in.co.application.chutneyApplication.entity.OrderItem;
import in.co.application.chutneyApplication.entity.Orders;
import in.co.application.chutneyApplication.repository.CartRepository;
import in.co.application.chutneyApplication.repository.OrderItemRepository;
import in.co.application.chutneyApplication.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;

@Service
@Transactional
public class OrderService {

    @Value("${razorpay.key.id}")
    private String razorpayKey;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;

    private final EmailService emailService;
    private final PdfService pdfService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartRepository cartRepository, EmailService emailService, PdfService pdfService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.emailService = emailService;
        this.pdfService = pdfService;
    }

    public Orders placeOrder(OrderRequestDto dto) {

        try {

            Orders order = new Orders();

            order.setName(dto.getName());
            order.setPhone(dto.getPhone());
            order.setAddress(dto.getAddress());
            order.setCountry(dto.getCountry());
            order.setTotalAmount(dto.getTotalAmount());
            order.setEmail(dto.getEmail());
            order.setStatus("CREATED");

            Orders savedOrder = orderRepository.save(order);

            // SAVE ORDER ITEMS
            if (dto.getItems() != null) {
                for (var item : dto.getItems()) {

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderId(savedOrder.getId());
                    orderItem.setProductId(item.getProductId());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(item.getPrice());

                    orderItemRepository.save(orderItem);
                }
            }

            RazorpayClient client = new RazorpayClient(razorpayKey, razorpaySecret);
            JSONObject options = new JSONObject();
            options.put("amount", dto.getTotalAmount() * 100);
            options.put("currency", "INR");
            options.put("receipt", savedOrder.getId());

            Order razorpayOrder = client.orders.create(options);
            savedOrder.setRazorpayOrderId(razorpayOrder.get("id"));
            orderRepository.save(savedOrder);
            return savedOrder;

        } catch (Exception ex) {
            System.out.println("RAZORPAY ERROR: " + ex.getMessage());
            throw new RuntimeException("Unable to connect to payment gateway. Please try again later.");
        }
    }

    public boolean verifyPayment(String orderId,
                                 String paymentId,
                                 String signature) throws Exception {

        String payload = orderId + "|" + paymentId;

        String expectedSignature = Utils.getHash(payload, razorpaySecret);

        if (expectedSignature.equals(signature)) {

            Orders order = orderRepository.findByRazorpayOrderId(orderId);

            if (order == null) {
                throw new RuntimeException("Order not found");
            }
            order.setRazorpayPaymentId(paymentId);
            order.setRazorpaySignature(signature);
            order.setStatus("PAID");

            orderRepository.save(order);

            // GENERATE PDF
            byte[] pdf = pdfService.generateInvoice(
                    order.getId(),
                    order.getName(),
                    order.getTotalAmount()
            );

            // EMAIL HTML
            String html = """
                    <div style="font-family: Arial, sans-serif; max-width:600px; margin:auto; border:1px solid #eee; border-radius:8px; overflow:hidden">
                    
                      <div style="background:#2f855a; color:white; padding:20px; text-align:center">
                        <h2 style="margin:0">🎉 Order Confirmed!</h2>
                        <p style="margin:5px 0 0">Thank you for choosing <b>Ghar Ka Zaika</b></p>
                      </div>
                    
                      <div style="padding:25px">
                    
                        <p style="font-size:16px">
                          Dear <b>%s</b>,
                        </p>
                    
                        <p style="font-size:15px; color:#444">
                          Your order has been <b style="color:#2f855a">successfully placed</b> and is now being prepared with love and fresh ingredients.
                          We truly appreciate your trust in <b>Ghar Ka Zaika</b>.
                        </p>
                    
                        <div style="background:#f7fafc; padding:15px; border-radius:6px; margin:20px 0">
                          <p style="margin:5px 0"><b>Order ID:</b> %s</p>
                          <p style="margin:5px 0"><b>Total Amount:</b> ₹%s</p>
                        </div>
                    
                        <p style="font-size:15px; color:#444">
                          📄 Your invoice is attached with this email for your reference.
                        </p>
                    
                        <p style="font-size:15px; color:#444">
                          If you have any questions, feel free to reply to this email.  
                          We’re always happy to help!
                        </p>
                    
                        <p style="margin-top:25px">
                          Warm regards,<br>
                          <b>Team Ghar Ka Zaika</b>
                        </p>
                    
                      </div>
                    
                      <div style="background:#f1f1f1; padding:12px; text-align:center; font-size:13px; color:#777">
                        Bringing homemade taste to your table ❤️
                      </div>
                    
                    </div>
                    """.formatted(
                    order.getName(),
                    order.getId(),
                    order.getTotalAmount()
            );

            // SEND EMAIL TO CUSTOMER
            emailService.sendOrderEmail(
                    order.getEmail(),
                    "Order Confirmation",
                    html,
                    pdf
            );

            // SEND EMAIL TO ADMIN
            emailService.sendOrderEmail(
                    "noorinkr1411@gmail.com",
                    "New Order Received",
                    html,
                    pdf
            );

            return true;
        }

        return false;
    }
}