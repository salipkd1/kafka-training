package com.smsservice.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;


@Service
public class OrderNotificationConsumer {

  @KafkaListener(id = "sms-notification-consumer",
      topics = "order-notification")
  @Retryable(value = { RuntimeException.class }, maxAttempts = 5, backoff = @Backoff(delay = 2000))
  public void listen(@Payload String message,
      @Header(name = "system", required = false) String system,
      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
      Acknowledgment ack) {

    System.out.println(String.format(
        "Received message in sms service: '%s' from topic: '%s', partition: %d, system: '%s', timestamp: %d",
        message, topic, partition, system, ts));

    // Simulate processing
    if (Math.random() < 0.5) { // Simulate a random failure
      throw new RuntimeException("Simulated processing error");
    }

    ack.acknowledge(); //for consumer acknowledgement
  }
}
