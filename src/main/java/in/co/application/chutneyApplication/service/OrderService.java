package in.co.application.chutneyApplication.service;

import in.co.application.chutneyApplication.dto.OrderItemDto;
import in.co.application.chutneyApplication.dto.OrderRequestDto;
import in.co.application.chutneyApplication.entity.OrderItem;
import in.co.application.chutneyApplication.entity.Orders;
import in.co.application.chutneyApplication.repository.CartRepository;
import in.co.application.chutneyApplication.repository.OrderItemRepository;
import in.co.application.chutneyApplication.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;

    private final EmailService emailService;
    private final PdfService pdfService;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CartRepository cartRepository,
                        EmailService emailService,
                        PdfService pdfService) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.emailService = emailService;
        this.pdfService = pdfService;
    }

    public Orders placeOrder(OrderRequestDto dto) {

        Orders order = new Orders();

        order.setName(dto.getName());
        order.setPhone(dto.getPhone());
        order.setAddress(dto.getAddress());
        order.setCountry(dto.getCountry());
        order.setTotalAmount(dto.getTotalAmount());
        order.setEmail(dto.getEmail());
        order.setStatus("PLACED");

        Orders savedOrder = orderRepository.save(order);

        if (dto.getItems() != null) {

            for (OrderItemDto item : dto.getItems()) {

                OrderItem orderItem = new OrderItem();

                orderItem.setOrderId(savedOrder.getId());
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(item.getPrice());

                orderItemRepository.save(orderItem);
            }
        }

        cartRepository.deleteAll();

        // Generate PDF
        byte[] pdf = pdfService.generateInvoice(
                savedOrder.getId(),
                savedOrder.getName(),
                savedOrder.getTotalAmount()
        );

        // HTML Email
        String html = """
        <h2>Order Confirmed ✅</h2>
        <p>Thank you for ordering from <b>Ghar Ki Chutney</b></p>

        <p><b>Order ID:</b> %s</p>
        <p><b>Customer:</b> %s</p>
        <p><b>Total:</b> ₹%s</p>

        <p>Your invoice is attached.</p>
        """.formatted(
                savedOrder.getId(),
                savedOrder.getName(),
                savedOrder.getTotalAmount()
        );

        // send email to customer
        emailService.sendOrderEmail(
                savedOrder.getEmail(),
                "Order Confirmation",
                html,
                pdf
        );

        // send email to admin
        emailService.sendOrderEmail(
                "noorinkr1411@gmail.com",
                "New Order Received",
                html,
                pdf
        );

        return savedOrder;
    }
    public Orders getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Orders updateOrder(Orders order) {
        return orderRepository.save(order);
    }
}