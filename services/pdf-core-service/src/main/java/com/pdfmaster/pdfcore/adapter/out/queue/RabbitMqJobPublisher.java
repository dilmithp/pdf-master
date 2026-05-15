package com.pdfmaster.pdfcore.adapter.out.queue;

import com.pdfmaster.pdfcore.application.port.out.JobPublisher;
import com.pdfmaster.pdfcore.domain.JobId;
import java.util.List;
import java.util.Map;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/** Publishes jobs to the {@code pdf.jobs} topic exchange with confirmed sends. */
@Component
public class RabbitMqJobPublisher implements JobPublisher {

  /** Topic exchange shared by all worker tiers. */
  public static final String EXCHANGE = "pdf.jobs";

  private final RabbitTemplate rabbitTemplate;

  public RabbitMqJobPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void publish(String op, JobId jobId, List<String> inputKeys, Map<String, Object> options) {
    Map<String, Object> payload =
        Map.of(
            "jobId",
            jobId.value().toString(),
            "inputKeys",
            inputKeys,
            "options",
            options == null ? Map.of() : options);
    String routingKey = "pdf." + op + ".requested";
    rabbitTemplate.convertAndSend(EXCHANGE, routingKey, payload);
  }
}
