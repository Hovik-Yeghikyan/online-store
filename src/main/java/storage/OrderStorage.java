package storage;



import model.Order;
import model.User;
import types.Answers;
import types.OrderStatus;
import types.PaymentMethod;
import util.StorageSerializeUtil;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class OrderStorage implements Serializable {

    private List<Order> orders = new LinkedList<>();

    public void add(Order order) {
        orders.add(order);
        StorageSerializeUtil.serializeOrderStorage(this);
    }

    public void printAllOrders() {
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    public void printMyOrders(User user) {
        for (Order order : orders) {
            if (order.getUser().equals(user)) {
                System.out.println(order);
            }
        }
    }


    public Order getOrderByID(String id) {
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }


    public PaymentMethod getOrderPayType(String type) {
        if (type.equals(PaymentMethod.CARD.name()) ||
                type.equals(PaymentMethod.CASH.name()) || type.equals(PaymentMethod.PAYPAL.name())) {
            return PaymentMethod.valueOf(type);
        }
        return null;
    }

    public OrderStatus getOrderStatusType(String type) {
        if (type.equals(OrderStatus.NEW.name()) ||
                type.equals(OrderStatus.DELIVERED.name()) || type.equals(OrderStatus.CANCELED.name())) {
            return OrderStatus.valueOf(type);
        }
        return null;
    }


    public Answers getAnswerType(String type) {
        if (type.equals(Answers.YES.name()) ||
                type.equals(Answers.NO.name())) {
            return Answers.valueOf(type);
        }
        return null;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
