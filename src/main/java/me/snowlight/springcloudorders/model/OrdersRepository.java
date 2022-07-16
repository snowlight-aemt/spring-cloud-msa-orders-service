package me.snowlight.springcloudorders.model;

import org.springframework.data.repository.CrudRepository;

public interface OrdersRepository extends CrudRepository<OrderEntity, Long> {
    public OrderEntity findByOrderId(String OrderId);
    public Iterable<OrderEntity> findByUserId(String userId);
}
