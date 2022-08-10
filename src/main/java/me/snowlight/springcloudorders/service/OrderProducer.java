package me.snowlight.springcloudorders.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.snowlight.springcloudorders.service.dto.Field;
import me.snowlight.springcloudorders.service.dto.KafkaOrderDto;
import me.snowlight.springcloudorders.service.dto.Payload;
import me.snowlight.springcloudorders.service.dto.Schema;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    List<Field> fields = Arrays.asList(new Field("string", true, "order_id"),
                                        new Field("string", true, "user_id"),
                                        new Field("string", true, "product_id"),
                                        new Field("int32", true, "qty"),
                                        new Field("int32", true, "total_price"),
                                        new Field("int32", true, "unit_price"));

    Schema schema = Schema.builder()
                    .type("struct")
                    .fields(fields)
                    .optional(false)
                    .name("orders")
                    .build();

    public OrderDto send(String kafkaTopic, OrderDto orderDto) {
        Payload payload = Payload.builder()
            .order_id(orderDto.getOrderId())
            .user_id(orderDto.getUserId())
            .product_id(orderDto.getProductId())
            .qty(orderDto.getQty())
            .unit_price(orderDto.getUnitPrice())
            .total_price(orderDto.getTotalPrice())
            .build();

        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(schema, payload);

        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(kafkaOrderDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(kafkaTopic, jsonInString);
        log.info("Order Producer send data from Order microservice: ", kafkaOrderDto);

        return orderDto;
    }
}
