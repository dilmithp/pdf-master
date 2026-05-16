package com.pdfmaster.pdfcore.config;

import com.pdfmaster.pdfcore.adapter.out.queue.RabbitMqJobPublisher;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares the inbound merge queue, its DLQ, and the {@code pdf.jobs} topic exchange. Producer-side
 * confirms are enabled so {@link RabbitMqJobPublisher} fails fast on broker errors.
 */
@Configuration
public class RabbitMqConfig {

  public static final String REQUEST_QUEUE = "pdf.merge.requested";
  public static final String DLQ_QUEUE = "pdf.merge.requested.dlq";
  public static final String ROUTING_KEY = "pdf.merge.requested";

  @Bean
  TopicExchange jobsExchange() {
    return new TopicExchange(RabbitMqJobPublisher.EXCHANGE, true, false);
  }

  @Bean
  Queue mergeQueue() {
    return QueueBuilder.durable(REQUEST_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
        .build();
  }

  @Bean
  Queue mergeDlq() {
    return QueueBuilder.durable(DLQ_QUEUE).build();
  }

  @Bean
  Binding mergeBinding(Queue mergeQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(mergeQueue).to(jobsExchange).with(ROUTING_KEY);
  }

  @Bean
  Jackson2JsonMessageConverter jsonMessageConverter() {
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    DefaultClassMapper mapper = new DefaultClassMapper();
    mapper.setTrustedPackages("com.pdfmaster.*", "java.util", "java.lang");
    converter.setClassMapper(mapper);
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
