package com.pdfmaster.pdfcore.application.port.out;

import com.pdfmaster.pdfcore.domain.PresignedUpload;
import java.net.URI;
import java.time.Duration;
import java.util.Map;

/** Outbound port abstracting object-storage (S3 in production, LocalStack in tests). */
public interface ObjectStore {

  PresignedUpload presignPut(String key, String contentType, Duration expiry);

  URI presignGet(String key, Duration expiry);

  void tag(String key, Map<String, String> tags);

  byte[] download(String key);

  void upload(String key, byte[] bytes, String contentType, Map<String, String> tags);
}
