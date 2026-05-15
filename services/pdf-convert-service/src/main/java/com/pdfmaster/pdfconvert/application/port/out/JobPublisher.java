package com.pdfmaster.pdfconvert.application.port.out;

import com.pdfmaster.pdfconvert.domain.JobId;
import java.util.List;
import java.util.Map;

/** Outbound port for publishing a job message to the broker. */
public interface JobPublisher {

  void publish(String op, JobId jobId, List<String> inputKeys, Map<String, Object> options);
}
