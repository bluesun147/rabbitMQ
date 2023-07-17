package com.example.rabbitMQ.rabbitMQ;

import com.example.rabbitMQ.RabbitMqApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

// 메시지 발행하는 클래스
@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public Runner(RabbitTemplate rabbitTemplate, Receiver receiver) {
        this.rabbitTemplate = rabbitTemplate;
        this.receiver = receiver;
    }

    // 스프링 빈 로딩된 후 run 실행
    @Override
    public void run(String... args) throws Exception {
        System.out.println("sending message...");
        // topic, key, message 지정해 메시지 발행
        rabbitTemplate.convertAndSend(RabbitMqApplication.topicExchangeName, "foo.bar.baz", "hello from rabbitMQ!!");
        // 리시버 객체가 가진 latch 활성화될 때까지 await
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }
}