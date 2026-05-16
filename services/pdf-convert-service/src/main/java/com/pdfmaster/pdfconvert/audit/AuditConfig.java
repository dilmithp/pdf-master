package com.pdfmaster.pdfconvert.audit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AuditConfig {

  public static final String ACCOUNT_EVENTS_EXCHANGE = "account.events";
  public static final String ACCOUNT_DELETED_ROUTING_KEY = "account.deleted";
  public static final String ACCOUNT_DELETED_QUEUE = "account.deleted.pdf-convert";

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
  public Queue accountDeletedPdfConvertQueue() {
    return new Queue(ACCOUNT_DELETED_QUEUE, true);
  }

  @Bean
  public Binding accountDeletedPdfConvertBinding(
      Queue accountDeletedPdfConvertQueue, TopicExchange accountEventsExchange) {
    return BindingBuilder.bind(accountDeletedPdfConvertQueue)
        .to(accountEventsExchange)
        .with(ACCOUNT_DELETED_ROUTING_KEY);
  }
}
