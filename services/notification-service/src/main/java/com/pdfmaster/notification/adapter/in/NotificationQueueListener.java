package com.pdfmaster.notification.adapter.in;

import com.pdfmaster.notification.application.NotificationService;
import com.pdfmaster.notification.domain.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** Consumes {@link NotificationEvent} messages from the {@code notification.queued} queue. */
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
