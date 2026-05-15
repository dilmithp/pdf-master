package com.pdfmaster.pdfai.application.port.out;

import com.pdfmaster.pdfai.domain.JobId;
import java.util.List;
import java.util.Map;

public interface JobPublisher {

  void publish(String op, JobId jobId, List<String> inputKeys, Map<String, Object> options);
}
