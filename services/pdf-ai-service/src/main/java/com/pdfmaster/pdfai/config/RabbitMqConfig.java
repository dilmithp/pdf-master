package com.pdfmaster.pdfai.config;

import com.pdfmaster.pdfai.adapter.out.queue.RabbitMqJobPublisher;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  public static final String REQUEST_QUEUE = "pdf.ai.requested";
  public static final String DLQ_QUEUE = "pdf.ai.requested.dlq";
  public static final String ROUTING_KEY = "pdf.ai.requested";

  @Bean
  TopicExchange jobsExchange() {
    return new TopicExchange(RabbitMqJobPublisher.EXCHANGE, true, false);
  }

  @Bean
  Queue aiQueue() {
    return QueueBuilder.durable(REQUEST_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
        .build();
  }

  @Bean
  Queue aiDlq() {
    return QueueBuilder.durable(DLQ_QUEUE).build();
  }

  @Bean
  Binding aiBinding(Queue aiQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(aiQueue).to(jobsExchange).with(ROUTING_KEY);
  }

  @Bean
  Jackson2JsonMessageConverter jsonMessageConverter() {
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
    typeMapper.setTrustedPackages("com.pdfmaster.*", "java.util", "java.lang");
    converter.setJavaTypeMapper(typeMapper);
    return converter;
  }

  @Bean
  RabbitTemplate rabbitTemplate(
      ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
    if (connectionFactory instanceof CachingConnectionFactory caching) {
      caching.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
      caching.setPublisherReturns(true);
    }
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(converter);
    template.setMandatory(true);
    template.setConfirmCallback(
        (correlationData, ack, cause) -> {
          if (!ack) {
            LoggerFactory.getLogger(RabbitMqConfig.class).warn("Publish nack: {}", cause);
          }
        });
    return template;
  }
}
