package me.snowlight.springcloudorders.controller;

import java.util.ArrayList;
import java.util.List;

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
import me.snowlight.springcloudorders.service.OrderDto;
import me.snowlight.springcloudorders.service.OrderService;


@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    private final ModelMapper modelMapper;
    @GetMapping("health_check")
    public ResponseEntity check(HttpServletRequest request) {
        return ResponseEntity.ok(String.format("It's Working in Order Service on PORT %s", request.getLocalPort()));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity CreateOrder(@RequestBody RequestOrder order, @PathVariable String userId) {
        OrderDto map = modelMapper.map(order, OrderDto.class);
        map.setUserId(userId);
        OrderDto orderDto = orderService.createOrder(map);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(orderDto, ResponseOrder.class));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable String userId) {
        List<ResponseOrder> result = new ArrayList<>();

        orderService.getOrdersByUserId(userId).forEach(orderEntity -> 
            result.add(modelMapper.map(orderEntity, ResponseOrder.class)));

        return ResponseEntity.ok(result);
    }
}
