package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.pdfmaster.pdfcore.adapter.out.s3.S3ObjectStore;
import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.config.RabbitMqConfig;
import com.pdfmaster.pdfcore.domain.JobId;
import com.pdfmaster.pdfcore.domain.JobRecord;
import com.pdfmaster.pdfcore.domain.JobStatus;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

/**
 * Full merge round-trip: uploads two PDFs to LocalStack S3, publishes a {@code pdf.merge.requested}
 * message to RabbitMQ, waits for {@link com.pdfmaster.pdfcore.adapter.in.MergeJobListener} to
 * consume it, and verifies the merged result lands in S3 with auto-delete tagging and the job is
 * marked SUCCEEDED.
 */
@SpringBootTest
@Testcontainers
class MergeEndToEndIntegrationTest {

  static final String BUCKET = "pdfmaster-merge-it";

  @Container @ServiceConnection
  static final RabbitMQContainer RABBIT =
      new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-management"));

  @Container
  static final LocalStackContainer LOCALSTACK =
      new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.8"))
          .withServices(LocalStackContainer.Service.S3);

  @DynamicPropertySource
  static void wireProps(DynamicPropertyRegistry registry) {
    registry.add("aws.s3.bucket", () -> BUCKET);
    registry.add("aws.s3.region", () -> LOCALSTACK.getRegion());
    registry.add(
        "aws.s3.endpoint",
        () -> LOCALSTACK.getEndpointOverride(LocalStackContainer.Service.S3).toString());
    registry.add("aws.s3.access-key", LOCALSTACK::getAccessKey);
    registry.add("aws.s3.secret-key", LOCALSTACK::getSecretKey);
  }

  @BeforeAll
  static void createBucket() {
    try (S3Client client =
        S3Client.builder()
            .endpointOverride(LOCALSTACK.getEndpointOverride(LocalStackContainer.Service.S3))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        LOCALSTACK.getAccessKey(), LOCALSTACK.getSecretKey())))
            .region(Region.of(LOCALSTACK.getRegion()))
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .build()) {
      client.createBucket(CreateBucketRequest.builder().bucket(BUCKET).build());
    }
  }

  @Autowired S3ObjectStore objectStore;
  @Autowired JobRepository jobRepository;
  @Autowired RabbitTemplate rabbitTemplate;

  @Test
  void publishedMergeJobIsConsumedAndProducesMergedPdf() throws Exception {
    JobId jobId = new JobId(UUID.randomUUID());
    String keyA = "uploads/" + jobId.value() + "/a.pdf";
    String keyB = "uploads/" + jobId.value() + "/b.pdf";

    objectStore.upload(keyA, twoPagePdf(), "application/pdf", Map.of());
    objectStore.upload(keyB, twoPagePdf(), "application/pdf", Map.of());

    jobRepository.save(
        JobRecord.queued(jobId, "merge", List.of(keyA, keyB), java.time.Instant.now(Clock.systemUTC())));

    rabbitTemplate.convertAndSend(
        com.pdfmaster.pdfcore.adapter.out.queue.RabbitMqJobPublisher.EXCHANGE,
        RabbitMqConfig.ROUTING_KEY,
        Map.of("jobId", jobId.value().toString(), "inputKeys", List.of(keyA, keyB)));

    await()
        .atMost(Duration.ofSeconds(20))
        .untilAsserted(
            () -> {
              JobRecord reloaded = jobRepository.findById(jobId).orElseThrow();
              assertThat(reloaded.status()).isEqualTo(JobStatus.SUCCEEDED);
              assertThat(reloaded.outputKey()).isPresent();
            });

    String outputKey = jobRepository.findById(jobId).orElseThrow().outputKey().orElseThrow();
    byte[] merged = objectStore.download(outputKey);
    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(merged)))) {
      assertThat(doc.getNumberOfPages()).isEqualTo(4);
    }

    Map<String, String> tags = objectStore.readTags(outputKey);
    assertThat(tags).containsEntry("auto-delete", "true");
  }

  static byte[] twoPagePdf() throws Exception {
    try (PDDocument doc = new PDDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      doc.addPage(new PDPage());
      doc.addPage(new PDPage());
      doc.save(out);
      return out.toByteArray();
    }
  }
}
