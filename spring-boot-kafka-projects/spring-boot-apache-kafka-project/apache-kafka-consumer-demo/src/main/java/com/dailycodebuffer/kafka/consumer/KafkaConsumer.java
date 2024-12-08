package com.dailycodebuffer.kafka.consumer;

import com.dailycodebuffer.kafka.model.Book;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(Book book) {
        System.out.println("@@@ message :::: " + book);
    }
}
