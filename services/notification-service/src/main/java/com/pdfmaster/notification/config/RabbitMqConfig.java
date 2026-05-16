package com.pdfmaster.notification.config;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** RabbitMQ topology for the notification service. */
@Configuration
public class RabbitMqConfig {

  public static final String EXCHANGE = "notifications";
  public static final String QUEUE = "notification.queued";
  public static final String ROUTING_KEY = "notification.#";

  @Bean
  public TopicExchange notificationsExchange() {
    return new TopicExchange(EXCHANGE, true, false);
  }

  @Bean
  public Queue notificationQueuedQueue() {
    return new Queue(QUEUE, true);
  }

  @Bean
  public Binding notificationBinding(
      Queue notificationQueuedQueue, TopicExchange notificationsExchange) {
    return BindingBuilder.bind(notificationQueuedQueue).to(notificationsExchange).with(ROUTING_KEY);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    DefaultClassMapper mapper = new DefaultClassMapper();
    mapper.setTrustedPackages("com.pdfmaster.*", "java.util", "java.lang");
    converter.setClassMapper(mapper);
    return converter;
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
      ConnectionFactory connectionFactory, MessageConverter messageConverter) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter);
    template.setConfirmCallback(
        (correlationData, ack, cause) -> {
          if (!ack) {
            LoggerFactory.getLogger(RabbitMqConfig.class).warn("Publish nack: {}", cause);
          }
        });
    return template;
  }
}
