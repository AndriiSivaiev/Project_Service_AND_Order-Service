package com.kafka.orderstatusservice.listener;


import com.kafka.orderservice.model.event.OrderEvent;
import com.kafka.orderservice.model.event.OrderStatusEvent;
import com.kafka.orderstatusservice.configuration.property.KafkaProperties;
import com.kafka.orderstatusservice.orderutil.OrderUtilClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

    private final KafkaProperties properties;

    @KafkaListener(
            topics = "${app.kafka.property.receiveTopic}",
            groupId = "${app.kafka.property.groupId}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload OrderEvent orderEvent,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) Long key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {

        log.info("Received message: {}", orderEvent);
        log.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestamp);

        OrderStatusEvent statusEvent = new OrderStatusEvent();
        statusEvent.setDate(Instant.now());
        statusEvent.setStatus(OrderUtilClass.getRandomStatusFromList(properties.getStatuses()));

        kafkaTemplate.send(properties.getSendTopic(), statusEvent);
    }
}