package com.kafka.orderstatusservice.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "app.kafka.property")
public class KafkaProperties {

    private String sendTopic;

    private String receiveTopic;

    private String groupId;

    private final List<String> statuses = new ArrayList<>();

}
