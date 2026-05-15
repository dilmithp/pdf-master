package com.pdfmaster.pdfocr.application.port.out;

import com.pdfmaster.pdfocr.domain.PresignedUpload;
import java.net.URI;
import java.time.Duration;
import java.util.Map;

public interface ObjectStore {

  PresignedUpload presignPut(String key, String contentType, Duration expiry);

  URI presignGet(String key, Duration expiry);

  void tag(String key, Map<String, String> tags);

  byte[] download(String key);

  void upload(String key, byte[] bytes, String contentType, Map<String, String> tags);
}
