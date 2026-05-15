package com.pdfmaster.pdfconvert.config;

import com.pdfmaster.pdfconvert.adapter.out.queue.RabbitMqJobPublisher;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  public static final String REQUEST_QUEUE = "pdf.convert.requested";
  public static final String DLQ_QUEUE = "pdf.convert.requested.dlq";
  public static final String ROUTING_KEY = "pdf.convert.requested";

  @Bean
  TopicExchange jobsExchange() {
    return new TopicExchange(RabbitMqJobPublisher.EXCHANGE, true, false);
  }

  @Bean
  Queue convertQueue() {
    return QueueBuilder.durable(REQUEST_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
        .build();
  }

  @Bean
  Queue convertDlq() {
    return QueueBuilder.durable(DLQ_QUEUE).build();
  }

  @Bean
  Binding convertBinding(Queue convertQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(convertQueue).to(jobsExchange).with(ROUTING_KEY);
  }

  @Bean
  Jackson2JsonMessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
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
    return template;
  }
}
