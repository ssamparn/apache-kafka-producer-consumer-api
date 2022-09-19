package com.kafka.consumer;

import com.kafka.consumer.domain.Item;
import com.kafka.deserializer.ItemDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ItemConsumer {

    private final String topicName = "item-topic";
    private KafkaConsumer<Integer, Item> kafkaConsumer;

    public ItemConsumer(Map<String, Object> propsMap){
        kafkaConsumer = new KafkaConsumer<Integer, Item>(propsMap);
    }

    public static void main(String[] args) {
        Map<String, Object> propsMap = buildConsumerProperties();
        ItemConsumer itemConsumer = new ItemConsumer(propsMap);
        itemConsumer.pollKafka();
    }

    public static Map<String, Object> buildConsumerProperties() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ItemDeserializer.class.getName());
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, "itemConsumer");

        //max.poll.interval.ms
        // propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "5000");

        //commit strategy
        //propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        // propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return propsMap;
    }

    public void pollKafka(){
        kafkaConsumer.subscribe(List.of(topicName));
        Duration timeOutDuration = Duration.of(100, ChronoUnit.MILLIS);

        try{
            while(true) {
                ConsumerRecords<Integer, Item> consumerRecords = kafkaConsumer.poll(timeOutDuration);
                consumerRecords.forEach((record) -> {
                    log.info("Consumed Record key is: {}  and the value is: {} and the partition is: {}", record.key(), record.value(), record.partition());
                });
            }
        } catch (Exception e) {
            log.error("Exception in pollKafka : {}", e.getMessage());
        } finally {
            kafkaConsumer.close(); // always close the consumer for releasing the connections and sockets
        }
    }
}