package com.example.rabbitMQ;

import com.example.rabbitMQ.rabbitMQ.Receiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// https://it-techtree.tistory.com/entry/messaging-with-rabbitMQ-in-springBoot

/*
메시지 생성,수신 흐름
publisher -> exchange -> queue -> consumer
- exchange : 발생된 메시지 큐에 저장하는 모듈
- queue : 메시지 저장 버퍼
- route : exchange 모둘이 큐로 메시지 전달 시 사용하는 키
- topic : 라우팅 키 패턴과 일피하는 큐에 모두 메시지 전송. multicast 방식
*/
@SpringBootApplication
public class RabbitMqApplication {

	public static final String topicExchangeName = "spring-boot-exchange";
	static final String queueName = "spring-boot";

	// 큐 이름 지정
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	// topic 지정
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(topicExchangeName);
	}

	// 큐와 토픽, 이를 연결한 라우트 키(foo.bar.#)로 연결
	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
	}

	// rabbitMQ 서버 연결, 메시지 수신할 큐와 메시지 수신 시 실행할 리스너 메서드 지정
	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
											 MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	// 메시지 수신 시 응답할 메서드 등록
	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(String[] args) {
		SpringApplication.run(RabbitMqApplication.class, args);
	}
}