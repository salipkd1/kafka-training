package com.orderservice.controller;


import com.orderservice.service.OrderNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor()
public class OrderController {

  private final OrderNotificationService orderNotificationService;

  @PostMapping(value = "/notification")
  public ResponseEntity<String> orderNotification() {

    String message= orderNotificationService.produce();

    return ResponseEntity.accepted().body(message);
  }

}
