package com.orderservice.service;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor()
public class OrderNotificationService {

  public static final String TOPIC = "order-notification";
  private final KafkaTemplate<String, String> kafkaTemplate;
  AtomicInteger counter = new AtomicInteger();

  public String produce() {

    var message = MessageBuilder.withPayload("Order " + counter.getAndIncrement())
        .setHeader(KafkaHeaders.TOPIC, TOPIC)
        .setHeader(KafkaHeaders.KEY, String.valueOf(counter.get()))
        .setHeader("system", "order-service")
        .build();


    var future = kafkaTemplate.send(message);

    try {
      var result = future.get();
      System.out.println("Message sent successfully: " + result.getProducerRecord().value());
      System.out.println("Partition: " + result.getRecordMetadata().partition());

      return result.getProducerRecord().value();
    } catch (ExecutionException | InterruptedException e) {
      System.err.println("Failed to send message: " + e.getCause().getMessage());
      return e.getCause().getMessage();
    }
  }
}
