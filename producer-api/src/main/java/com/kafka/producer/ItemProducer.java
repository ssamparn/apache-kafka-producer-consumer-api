package com.kafka.producer;

import com.kafka.producer.domain.Item;
import com.kafka.serializer.ItemSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
public class ItemProducer {

    private final String topicName = "item-topic";
    private KafkaProducer<Integer, Item> kafkaProducer;

    public ItemProducer(Map<String, Object> producerProps) {
        kafkaProducer = new KafkaProducer<>(producerProps);
    }

    public static void main(String[] args) {
        ItemProducer itemProducer = new ItemProducer(propsMap());
        List<Item> items = List.of(new Item(1, "LG TV", 200.00),
                new Item(2, "Iphone 10 Pro Max", 949.99));
        items.forEach((itemProducer::publishMessageSync));
    }

    public static Map<String, Object> propsMap() {

        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        propsMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        propsMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ItemSerializer.class.getName());
        propsMap.put(ProducerConfig.ACKS_CONFIG, "all");
        propsMap.put(ProducerConfig.RETRIES_CONFIG, "10");
        propsMap.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "3000");

        return propsMap;
    }

    private void publishMessageSync(Item item) {
        ProducerRecord<Integer, Item> producerRecord = new ProducerRecord<>(topicName, item.getId(), item);
        RecordMetadata recordMetadata;
        try {
            recordMetadata = kafkaProducer.send(producerRecord).get();
            log.info("Published Record Offset is : {} and the partition is : {}", recordMetadata.offset(), recordMetadata.partition());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Exception in publishMessageSync : {}", e.getMessage());
        }
    }
}