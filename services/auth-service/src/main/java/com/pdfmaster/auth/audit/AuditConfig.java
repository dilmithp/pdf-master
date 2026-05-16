package com.pdfmaster.auth.audit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Registers the audit filter and declares the account.events topology used for
 * publishing {@link AccountDeletedEvent} and receiving the cascade broadcast.
 */
@Configuration
@EnableAsync
public class AuditConfig {

  public static final String ACCOUNT_EVENTS_EXCHANGE = "account.events";
  public static final String ACCOUNT_DELETED_ROUTING_KEY = "account.deleted";
  public static final String ACCOUNT_DELETED_QUEUE = "account.deleted.auth";

  @Bean
  public FilterRegistrationBean<AuditFilter> auditFilter(AuditFilter filter) {
    FilterRegistrationBean<AuditFilter> reg = new FilterRegistrationBean<>(filter);
    reg.addUrlPatterns("/*");
    reg.setOrder(Integer.MAX_VALUE);
    return reg;
  }

  @Bean
  public TopicExchange accountEventsExchange() {
    return new TopicExchange(ACCOUNT_EVENTS_EXCHANGE, true, false);
  }

  @Bean
  public Queue accountDeletedQueue() {
    return new Queue(ACCOUNT_DELETED_QUEUE, true);
  }

  @Bean
  public Binding accountDeletedBinding(Queue accountDeletedQueue, TopicExchange accountEventsExchange) {
    return BindingBuilder.bind(accountDeletedQueue).to(accountEventsExchange).with(ACCOUNT_DELETED_ROUTING_KEY);
  }

  @Bean
  public Jackson2JsonMessageConverter auditJsonMessageConverter() {
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
    typeMapper.setTrustedPackages("com.pdfmaster.*", "java.util", "java.lang", "java.time");
    converter.setJavaTypeMapper(typeMapper);
    return converter;
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
      ConnectionFactory connectionFactory, Jackson2JsonMessageConverter auditJsonMessageConverter) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(auditJsonMessageConverter);
    return template;
  }
}
