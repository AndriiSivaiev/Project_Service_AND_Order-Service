package com.kafka.service;

import com.kafka.configuration.property.KafkaProperties;
import com.kafka.model.Order;
import com.kafka.model.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final List<Order> orders = new ArrayList<>();

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private final KafkaProperties properties;

    public Order createOrder(Order order) {
        order.setId(System.currentTimeMillis());
        orders.add(order);
        OrderEvent event = OrderEvent.orderToOrderEvent(order);
        kafkaTemplate.send(properties.getSendTopic(), event);

        return order;
    }

}
