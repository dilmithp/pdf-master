package com.pdfmaster.pdfocr.adapter.out.queue;

import com.pdfmaster.pdfocr.application.port.out.JobPublisher;
import com.pdfmaster.pdfocr.domain.JobId;
import java.util.List;
import java.util.Map;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqJobPublisher implements JobPublisher {

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
    rabbitTemplate.convertAndSend(EXCHANGE, "pdf." + op + ".requested", payload);
  }
}
