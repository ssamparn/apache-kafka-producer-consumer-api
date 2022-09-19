package com.kafka.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.producer.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class ItemSerializer implements Serializer<Item> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Item data) {
        log.info("Inside Item Serializer");

        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize object: {}", data);
            return null;
        }
    }
}