package com.kafka.model.event;

import com.kafka.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderEvent {

    private String product;

    private Integer quantity;

    public static OrderEvent orderToOrderEvent(Order order) {
        return new OrderEvent(order.getProduct(), order.getQuantity());
    }

}
