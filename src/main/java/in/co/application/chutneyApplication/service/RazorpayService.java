//package in.co.application.chutneyApplication.service;
//
//import com.razorpay.Order;
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RazorpayService {
//
//    private final RazorpayClient razorpayClient;
//
//    public RazorpayService(
//            @Value("${razorpay.key_id}") String keyId,
//            @Value("${razorpay.key_secret}") String keySecret
//    ) throws RazorpayException {
//        this.razorpayClient = new RazorpayClient(keyId, keySecret);
//    }
//
//    public Order createOrder(int amountInRupees, String currency, String receipt) throws RazorpayException {
//        JSONObject options = new JSONObject();
//        options.put("amount", amountInRupees * 100); // in paise
//        options.put("currency", currency != null ? currency : "INR");
//        options.put("receipt", receipt != null ? receipt : "receipt#1");
//        options.put("payment_capture", 1); // auto capture
//
//        return razorpayClient.orders.create(options);
//    }
//}