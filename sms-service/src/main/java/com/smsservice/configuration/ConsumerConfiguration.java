package com.smsservice.configuration;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;

@Configuration
public class ConsumerConfiguration {

  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "sms-notification-group");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    //props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Disable auto-commit

    props.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, 500);  // 500 ms between reconnect attempts
    props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);     // 1 second before retrying a request
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 20000);  // 20 seconds timeout
    props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 5000); // 5 seconds heartbeat interval

    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());

    // Set the acknowledgment mode to RECORD
    factory.getContainerProperties().setAckMode(AckMode.MANUAL);

    return factory;
  }
}
