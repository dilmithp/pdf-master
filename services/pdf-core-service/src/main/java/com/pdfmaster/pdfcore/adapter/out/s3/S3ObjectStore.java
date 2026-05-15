package com.pdfmaster.pdfcore.adapter.out.s3;

import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import com.pdfmaster.pdfcore.domain.PresignedUpload;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.s3.model.Tagging;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

/** AWS SDK v2 implementation of {@link ObjectStore}. All uploads are tagged auto-delete=true. */
@Component
public class S3ObjectStore implements ObjectStore {

  /** Tag applied to every uploaded object so the lifecycle policy reaps it after 60 min. */
  public static final String AUTO_DELETE_TAG_KEY = "auto-delete";

  public static final String AUTO_DELETE_TAG_VALUE = "true";

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final String bucket;

  public S3ObjectStore(S3Client s3Client, S3Presigner s3Presigner, String bucket) {
    this.s3Client = Objects.requireNonNull(s3Client, "s3Client");
    this.s3Presigner = Objects.requireNonNull(s3Presigner, "s3Presigner");
    this.bucket = Objects.requireNonNull(bucket, "bucket");
  }

  @Override
  public PresignedUpload presignPut(String key, String contentType, Duration expiry) {
    PutObjectRequest put =
        PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(contentType)
            .tagging(autoDeleteTagging())
            .build();
    PresignedPutObjectRequest presigned =
        s3Presigner.presignPutObject(
            PutObjectPresignRequest.builder()
                .signatureDuration(expiry)
                .putObjectRequest(put)
                .build());
    return new PresignedUpload(
        URI.create(presigned.url().toString()), key, Instant.now().plus(expiry));
  }

  @Override
  public URI presignGet(String key, Duration expiry) {
    GetObjectRequest get = GetObjectRequest.builder().bucket(bucket).key(key).build();
    PresignedGetObjectRequest presigned =
        s3Presigner.presignGetObject(
            GetObjectPresignRequest.builder()
                .signatureDuration(expiry)
                .getObjectRequest(get)
                .build());
    return URI.create(presigned.url().toString());
  }

  @Override
  public void tag(String key, Map<String, String> tags) {
    List<Tag> tagList = new ArrayList<>(tags.size());
    for (Map.Entry<String, String> e : tags.entrySet()) {
      tagList.add(Tag.builder().key(e.getKey()).value(e.getValue()).build());
    }
    s3Client.putObjectTagging(
        PutObjectTaggingRequest.builder()
            .bucket(bucket)
            .key(key)
            .tagging(Tagging.builder().tagSet(tagList).build())
            .build());
  }

  @Override
  public byte[] download(String key) {
    ResponseBytes<GetObjectResponse> bytes =
        s3Client.getObjectAsBytes(GetObjectRequest.builder().bucket(bucket).key(key).build());
    return bytes.asByteArray();
  }

  @Override
  public void upload(String key, byte[] bytes, String contentType, Map<String, String> tags) {
    Map<String, String> merged = new java.util.HashMap<>(tags == null ? Map.of() : tags);
    merged.putIfAbsent(AUTO_DELETE_TAG_KEY, AUTO_DELETE_TAG_VALUE);
    List<Tag> tagList = new ArrayList<>(merged.size());
    for (Map.Entry<String, String> e : merged.entrySet()) {
      tagList.add(Tag.builder().key(e.getKey()).value(e.getValue()).build());
    }
    PutObjectRequest put =
        PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(contentType)
            .tagging(Tagging.builder().tagSet(tagList).build())
            .build();
    s3Client.putObject(put, RequestBody.fromBytes(bytes));
  }

  /** Exposed for tests / observability. */
  public Map<String, String> readTags(String key) {
    var resp =
        s3Client.getObjectTagging(
            GetObjectTaggingRequest.builder().bucket(bucket).key(key).build());
    Map<String, String> out = new java.util.HashMap<>(resp.tagSet().size());
    for (Tag t : resp.tagSet()) {
      out.put(t.key(), t.value());
    }
    return out;
  }

  public String bucket() {
    return bucket;
  }

  private static Tagging autoDeleteTagging() {
    return Tagging.builder()
        .tagSet(Tag.builder().key(AUTO_DELETE_TAG_KEY).value(AUTO_DELETE_TAG_VALUE).build())
        .build();
  }
}
