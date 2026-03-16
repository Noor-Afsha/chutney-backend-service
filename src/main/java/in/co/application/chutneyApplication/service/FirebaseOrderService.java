package in.co.application.chutneyApplication.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import in.co.application.chutneyApplication.entity.Orders;
import org.springframework.stereotype.Service;

@Service
public class FirebaseOrderService {

    public void sendOrderToFirebase(Orders order) {

        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference("orders")
                .child(order.getId().toString());

        ref.setValueAsync(order);
    }
}