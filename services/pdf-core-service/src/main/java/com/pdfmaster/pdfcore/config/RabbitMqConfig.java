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
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares all inbound queues (and their DLQs) for every PDF-core operation, plus the {@code
 * pdf.jobs} topic exchange. Producer-side confirms are enabled so {@link RabbitMqJobPublisher}
 * fails fast on broker errors.
 */
@Configuration
public class RabbitMqConfig {

  // ---- merge ----
  public static final String REQUEST_QUEUE = "pdf.merge.requested";
  public static final String DLQ_QUEUE = "pdf.merge.requested.dlq";
  public static final String ROUTING_KEY = "pdf.merge.requested";

  // ---- split ----
  public static final String SPLIT_QUEUE = "pdf.split.requested";
  public static final String SPLIT_DLQ = "pdf.split.requested.dlq";

  // ---- compress ----
  public static final String COMPRESS_QUEUE = "pdf.compress.requested";
  public static final String COMPRESS_DLQ = "pdf.compress.requested.dlq";

  // ---- rotate ----
  public static final String ROTATE_QUEUE = "pdf.rotate.requested";
  public static final String ROTATE_DLQ = "pdf.rotate.requested.dlq";

  // ---- watermark ----
  public static final String WATERMARK_QUEUE = "pdf.watermark.requested";
  public static final String WATERMARK_DLQ = "pdf.watermark.requested.dlq";

  // ---- page-numbers ----
  public static final String PAGE_NUMBERS_QUEUE = "pdf.page-numbers.requested";
  public static final String PAGE_NUMBERS_DLQ = "pdf.page-numbers.requested.dlq";

  // ---- unlock ----
  public static final String UNLOCK_QUEUE = "pdf.unlock.requested";
  public static final String UNLOCK_DLQ = "pdf.unlock.requested.dlq";

  // ---- protect ----
  public static final String PROTECT_QUEUE = "pdf.protect.requested";
  public static final String PROTECT_DLQ = "pdf.protect.requested.dlq";

  // ---- extract-pages ----
  public static final String EXTRACT_PAGES_QUEUE = "pdf.extract-pages.requested";
  public static final String EXTRACT_PAGES_DLQ = "pdf.extract-pages.requested.dlq";

  // ---- reorder-pages ----
  public static final String REORDER_PAGES_QUEUE = "pdf.reorder-pages.requested";
  public static final String REORDER_PAGES_DLQ = "pdf.reorder-pages.requested.dlq";

  // ---- delete-pages ----
  public static final String DELETE_PAGES_QUEUE = "pdf.delete-pages.requested";
  public static final String DELETE_PAGES_DLQ = "pdf.delete-pages.requested.dlq";

  // ---- nup ----
  public static final String NUP_QUEUE = "pdf.nup.requested";
  public static final String NUP_DLQ = "pdf.nup.requested.dlq";

  // ---- crop ----
  public static final String CROP_QUEUE = "pdf.crop.requested";
  public static final String CROP_DLQ = "pdf.crop.requested.dlq";

  @Bean
  TopicExchange jobsExchange() {
    return new TopicExchange(RabbitMqJobPublisher.EXCHANGE, true, false);
  }

  // ---- merge beans ----

  @Bean
  Queue mergeQueue() {
    return opQueue(REQUEST_QUEUE, DLQ_QUEUE);
  }

  @Bean
  Queue mergeDlq() {
    return QueueBuilder.durable(DLQ_QUEUE).build();
  }

  @Bean
  Binding mergeBinding(Queue mergeQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(mergeQueue).to(jobsExchange).with(ROUTING_KEY);
  }

  // ---- split beans ----

  @Bean
  Queue splitQueue() {
    return opQueue(SPLIT_QUEUE, SPLIT_DLQ);
  }

  @Bean
  Queue splitDlq() {
    return QueueBuilder.durable(SPLIT_DLQ).build();
  }

  @Bean
  Binding splitBinding(Queue splitQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(splitQueue).to(jobsExchange).with(SPLIT_QUEUE);
  }

  // ---- compress beans ----

  @Bean
  Queue compressQueue() {
    return opQueue(COMPRESS_QUEUE, COMPRESS_DLQ);
  }

  @Bean
  Queue compressDlq() {
    return QueueBuilder.durable(COMPRESS_DLQ).build();
  }

  @Bean
  Binding compressBinding(Queue compressQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(compressQueue).to(jobsExchange).with(COMPRESS_QUEUE);
  }

  // ---- rotate beans ----

  @Bean
  Queue rotateQueue() {
    return opQueue(ROTATE_QUEUE, ROTATE_DLQ);
  }

  @Bean
  Queue rotateDlq() {
    return QueueBuilder.durable(ROTATE_DLQ).build();
  }

  @Bean
  Binding rotateBinding(Queue rotateQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(rotateQueue).to(jobsExchange).with(ROTATE_QUEUE);
  }

  // ---- watermark beans ----

  @Bean
  Queue watermarkQueue() {
    return opQueue(WATERMARK_QUEUE, WATERMARK_DLQ);
  }

  @Bean
  Queue watermarkDlq() {
    return QueueBuilder.durable(WATERMARK_DLQ).build();
  }

  @Bean
  Binding watermarkBinding(Queue watermarkQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(watermarkQueue).to(jobsExchange).with(WATERMARK_QUEUE);
  }

  // ---- page-numbers beans ----

  @Bean
  Queue pageNumbersQueue() {
    return opQueue(PAGE_NUMBERS_QUEUE, PAGE_NUMBERS_DLQ);
  }

  @Bean
  Queue pageNumbersDlq() {
    return QueueBuilder.durable(PAGE_NUMBERS_DLQ).build();
  }

  @Bean
  Binding pageNumbersBinding(Queue pageNumbersQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(pageNumbersQueue).to(jobsExchange).with(PAGE_NUMBERS_QUEUE);
  }

  // ---- unlock beans ----

  @Bean
  Queue unlockQueue() {
    return opQueue(UNLOCK_QUEUE, UNLOCK_DLQ);
  }

  @Bean
  Queue unlockDlq() {
    return QueueBuilder.durable(UNLOCK_DLQ).build();
  }

  @Bean
  Binding unlockBinding(Queue unlockQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(unlockQueue).to(jobsExchange).with(UNLOCK_QUEUE);
  }

  // ---- protect beans ----

  @Bean
  Queue protectQueue() {
    return opQueue(PROTECT_QUEUE, PROTECT_DLQ);
  }

  @Bean
  Queue protectDlq() {
    return QueueBuilder.durable(PROTECT_DLQ).build();
  }

  @Bean
  Binding protectBinding(Queue protectQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(protectQueue).to(jobsExchange).with(PROTECT_QUEUE);
  }

  // ---- extract-pages beans ----

  @Bean
  Queue extractPagesQueue() {
    return opQueue(EXTRACT_PAGES_QUEUE, EXTRACT_PAGES_DLQ);
  }

  @Bean
  Queue extractPagesDlq() {
    return QueueBuilder.durable(EXTRACT_PAGES_DLQ).build();
  }

  @Bean
  Binding extractPagesBinding(Queue extractPagesQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(extractPagesQueue).to(jobsExchange).with(EXTRACT_PAGES_QUEUE);
  }

  // ---- reorder-pages beans ----

  @Bean
  Queue reorderPagesQueue() {
    return opQueue(REORDER_PAGES_QUEUE, REORDER_PAGES_DLQ);
  }

  @Bean
  Queue reorderPagesDlq() {
    return QueueBuilder.durable(REORDER_PAGES_DLQ).build();
  }

  @Bean
  Binding reorderPagesBinding(Queue reorderPagesQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(reorderPagesQueue).to(jobsExchange).with(REORDER_PAGES_QUEUE);
  }

  // ---- delete-pages beans ----

  @Bean
  Queue deletePagesQueue() {
    return opQueue(DELETE_PAGES_QUEUE, DELETE_PAGES_DLQ);
  }

  @Bean
  Queue deletePagesDlq() {
    return QueueBuilder.durable(DELETE_PAGES_DLQ).build();
  }

  @Bean
  Binding deletePagesBinding(Queue deletePagesQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(deletePagesQueue).to(jobsExchange).with(DELETE_PAGES_QUEUE);
  }

  // ---- nup beans ----

  @Bean
  Queue nupQueue() {
    return opQueue(NUP_QUEUE, NUP_DLQ);
  }

  @Bean
  Queue nupDlq() {
    return QueueBuilder.durable(NUP_DLQ).build();
  }

  @Bean
  Binding nupBinding(Queue nupQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(nupQueue).to(jobsExchange).with(NUP_QUEUE);
  }

  // ---- crop beans ----

  @Bean
  Queue cropQueue() {
    return opQueue(CROP_QUEUE, CROP_DLQ);
  }

  @Bean
  Queue cropDlq() {
    return QueueBuilder.durable(CROP_DLQ).build();
  }

  @Bean
  Binding cropBinding(Queue cropQueue, TopicExchange jobsExchange) {
    return BindingBuilder.bind(cropQueue).to(jobsExchange).with(CROP_QUEUE);
  }

  // ---- shared infrastructure ----

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

  private static Queue opQueue(String name, String dlqName) {
    return QueueBuilder.durable(name)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", dlqName)
        .build();
  }
}
