package me.snowlight.springcloudorders.service.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class KafkaOrderDto implements Serializable {
    private Schema schema;
    private Payload payload;
}