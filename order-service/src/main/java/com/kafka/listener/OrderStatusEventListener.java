package com.kafka.listener;

import com.kafka.model.event.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderStatusEventListener {

    @KafkaListener(
            topics = "${app.kafka.property.receiveTopic}",
            groupId = "${app.kafka.property.groupId}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload OrderStatusEvent statusEvent,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) Long key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {

        log.info("Received message: {}", statusEvent);
        log.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestamp);
    }
}
