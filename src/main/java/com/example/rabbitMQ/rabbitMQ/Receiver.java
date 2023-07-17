package com.example.rabbitMQ.rabbitMQ;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/*
SpringBoot는 발행자(pub), 구독자(sub)가 됨
RabbitMQ 서버는 broker.
*/

// 메시지 구독 위한 receiver 클래스
// 수신받은 메시지 출력하고 latch의 카운트 감소
@Component
public class Receiver {
    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Received < " + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return  latch;
    }
}