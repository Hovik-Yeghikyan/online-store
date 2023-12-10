package model;



import lombok.*;
import types.OrderStatus;
import types.PaymentMethod;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    private String id;
    private User user;
    private String product;
    private Date date;
    private double price;
    private OrderStatus orderStatus;
    private int qty;
    private PaymentMethod paymentMethod;


}
