package com.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MessageProducer {

    private static final Logger log = LoggerFactory.getLogger(MessageProducer.class);

    private String topicName = "test-topic";

    private KafkaProducer<String, String> kafkaProducer;

    public MessageProducer(Map<String, Object> propsMap) {
        this.kafkaProducer = new KafkaProducer<>(propsMap);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        MessageProducer messageProducer = new MessageProducer(propsMap());
        messageProducer.publishMessageSync(null, "ABC-Sync");
        messageProducer.publishMessageAsync(null, "ABC-Async");
    }

    public void publishMessageSync(String key, String message) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, key, message);

        try {
            RecordMetadata recordMetadata = kafkaProducer.send(producerRecord).get();
            logSuccessResponse(key, message, recordMetadata);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Exception in publishMessageSync : {} ", e.getMessage());
        }
    }

    public void publishMessageAsync(String key, String message) throws InterruptedException, ExecutionException {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, key, message);
        Future<RecordMetadata> recordMetadataFuture = kafkaProducer.send(producerRecord, callback);
        logSuccessResponse(key, message, recordMetadataFuture.get());
        Thread.sleep(3000);
    }

    Callback callback = (recordMetadata, exception) -> {
        if (exception != null) {
            log.error("Exception is {} ", exception.getMessage());
        } else {
            log.info("Record MetaData Async in CallBack Offset : {} and the partition is {}", recordMetadata.offset(), recordMetadata.partition());
        }
    };

    public static Map<String, Object> propsMap() {
        Map<String, Object> propsMap = new HashMap<>();

        propsMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        propsMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        propsMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        propsMap.put(ProducerConfig.ACKS_CONFIG, "all");

        return propsMap;
    }

    public void logSuccessResponse(String key, String message, RecordMetadata recordMetadata) {
        log.info("Message '{}' sent successfully with the key  '{}' ", message, key);
        log.info(" Published Record Offset is {} and the partition is {}", recordMetadata.offset(), recordMetadata.partition());
    }


    public void close() {
        kafkaProducer.close();
    }
}
