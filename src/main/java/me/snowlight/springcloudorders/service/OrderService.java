package me.snowlight.springcloudorders.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.snowlight.springcloudorders.model.OrderEntity;
import me.snowlight.springcloudorders.model.OrdersRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrdersRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderDto createOrder(OrderDto orderDetails) {
        orderDetails.setOrderId(UUID.randomUUID().toString());
        orderDetails.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

        OrderEntity orderEntity = modelMapper.map(orderDetails, OrderEntity.class);
        orderRepository.save(orderEntity);

        return modelMapper.map(orderEntity, OrderDto.class);
    }

    public OrderDto getOrdersbyOrderId(String orderId) {
        return modelMapper.map(orderRepository.findByOrderId(orderId), OrderDto.class);
    }

    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
