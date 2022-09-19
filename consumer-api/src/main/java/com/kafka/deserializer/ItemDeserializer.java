package com.kafka.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.consumer.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
public class ItemDeserializer implements Deserializer<Item> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Item deserialize(String topic, byte[] data) {
        log.info("Inside Item Deserializer");
        Item item = null;
        try {
            item = objectMapper.readValue(data, Item.class);
        } catch (IOException e) {
            log.error("Exception Occurred during deserializing: {}", e.getMessage());
        }
        return item;
    }
}