package com.dailycodebuffer.kafka.controller;

import com.dailycodebuffer.kafka.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoController {

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, Book> kafkaTemplate;


    @PostMapping("/publish")
    public String publishMessage(@RequestBody Book book) {
        kafkaTemplate.send(topicName, book);
        return "Published Successfully!";
    }
}
