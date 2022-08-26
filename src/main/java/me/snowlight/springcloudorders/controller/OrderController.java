package me.snowlight.springcloudorders.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.snowlight.springcloudorders.service.KafkaProducer;
import me.snowlight.springcloudorders.service.OrderDto;
import me.snowlight.springcloudorders.service.OrderProducer;
import me.snowlight.springcloudorders.service.OrderService;


@Slf4j
@RestController
// @RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;
    
    private final OrderService orderService;

    private final ModelMapper modelMapper;

    @GetMapping("health_check")
    public ResponseEntity check(HttpServletRequest request) {
        return ResponseEntity.ok(String.format("It's Working in Order Service on PORT %s", request.getLocalPort()));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> CreateOrder(@RequestBody RequestOrder order, @PathVariable String userId) {
        log.info("Before added orders data");
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        orderDto.setUserId(userId);

        /* JPA */
        OrderDto createDto = orderService.createOrder(orderDto);
        ResponseOrder returnValue = modelMapper.map(createDto, ResponseOrder.class);
        
        // orderDto.setOrderId(UUID.randomUUID().toString());
        // orderDto.setTotalPrice(order.getQty() * order.getUnitPrice());
        
        /* Kafka */
        // orderProducer.send("orders", orderDto);
        kafkaProducer.send("example-order-topic", orderDto);

        // ResponseOrder returnValue = modelMapper.map(orderDto, ResponseOrder.class);

        log.info("After added orders data");
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable String userId) throws Exception{
        log.info("Before retrieve orders data");
        List<ResponseOrder> result = new ArrayList<>();

        orderService.getOrdersByUserId(userId).forEach(orderEntity -> 
            result.add(modelMapper.map(orderEntity, ResponseOrder.class)));

        log.info("After retrieve orders data");
        return ResponseEntity.ok(result);
    }
}
