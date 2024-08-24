package com.kafka.configuration.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.kafka.property")
public class KafkaProperties {

    private String sendTopic;

    private String receiveTopic;

    private String groupId;

}
