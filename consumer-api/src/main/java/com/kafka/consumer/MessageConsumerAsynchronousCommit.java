package com.kafka.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageConsumerAsynchronousCommit {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumerAsynchronousCommit.class);

    private KafkaConsumer<String, String> kafkaConsumer;
    private String topicName = "test-topic";

    public static void main(String[] args) {
        MessageConsumerAsynchronousCommit messageConsumer = new MessageConsumerAsynchronousCommit(buildConsumerProperties());
        messageConsumer.pollKafka();
    }

    public MessageConsumerAsynchronousCommit(Map<String, Object> propsMap) {
        kafkaConsumer = new KafkaConsumer<String, String>(propsMap);
    }

    public static Map<String, Object> buildConsumerProperties() {

        Map<String, Object> propsMap = new HashMap<>();

        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, "messageConsumer");
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        return propsMap;
    }

    public void pollKafka() {
        kafkaConsumer.subscribe(List.of(topicName));
        Duration timeOutDuration = Duration.of(100, ChronoUnit.MILLIS);
        try {
            while (true) {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(timeOutDuration);

                consumerRecords.forEach((record) -> {
                    logger.info("Consumer Record Key is {} and the value is {} and the partion {}",
                            record.key(), record.value(), record.partition());
                });

                if (consumerRecords.count() > 0) {
                    kafkaConsumer.commitAsync((offSets, exception) -> {
                        if (exception != null) {
                            logger.error("Exception while committing the offsets : {} ", exception.getMessage());
                        } else {
                            logger.info("Offsets are committed successfully");
                        }
                    }); // commits the last record offset read by the poll invocation
                }
            }
        } catch (CommitFailedException e) {
            logger.error("CommitFailedException in pollKafka : " + e);
        } catch (Exception e) {
            logger.error("Exception in pollKafka : " + e);
        } finally {
            kafkaConsumer.close();
        }
    }


}
