package com.pdfmaster.notification.adapter.in;

import com.pdfmaster.notification.application.NotificationService;
import com.pdfmaster.notification.domain.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes {@link NotificationEvent} messages from the {@code notification.queued} queue.
 *
 * <p>Acknowledgement mode: Spring AMQP defaults to auto-ack — the framework acks the message
 * immediately after the listener method returns normally. If an exception propagates, Spring AMQP's
 * {@code SimpleRetryPolicy} retries up to the configured max-attempts, then routes the message to
 * the dead-letter queue (DLQ) configured in {@link com.pdfmaster.notification.config.RabbitMqConfig}.
 * Switching to manual ack ({@code AcknowledgeMode.MANUAL}) would require the method to accept a
 * {@code Channel} + {@code deliveryTag} and call {@code channel.basicAck/basicNack} — defer until
 * at-least-once with idempotent consumers is fully implemented.
 */
@Component
public class NotificationQueueListener {

  private static final Logger log = LoggerFactory.getLogger(NotificationQueueListener.class);

  private final NotificationService service;

  public NotificationQueueListener(NotificationService service) {
    this.service = service;
  }

  @RabbitListener(queues = "notification.queued")
  public void onMessage(NotificationEvent event) {
    String messageId =
        service.send(event.templateCode(), event.locale(), event.recipient(), event.vars());
    log.info(
        "Sent queued notification template={} locale={} messageId={}",
        event.templateCode(),
        event.locale(),
        messageId);
  }
}
