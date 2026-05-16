package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pdfmaster.pdfcore.audit.SpringDataAuditLogRepository;
import java.time.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
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

@SpringBootTest(properties = {"spring.autoconfigure.exclude="})
@AutoConfigureMockMvc
@Testcontainers
class AuditFilterIntegrationTest {

  static final String BUCKET = "pdfmaster-audit-core";

  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

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
    registry.add("aws.s3.endpoint",
        () -> LOCALSTACK.getEndpointOverride(LocalStackContainer.Service.S3).toString());
    registry.add("aws.s3.access-key", LOCALSTACK::getAccessKey);
    registry.add("aws.s3.secret-key", LOCALSTACK::getSecretKey);
  }

  @BeforeAll
  static void createBucket() {
    try (S3Client client = S3Client.builder()
        .endpointOverride(LOCALSTACK.getEndpointOverride(LocalStackContainer.Service.S3))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(LOCALSTACK.getAccessKey(), LOCALSTACK.getSecretKey())))
        .region(Region.of(LOCALSTACK.getRegion()))
        .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
        .build()) {
      client.createBucket(CreateBucketRequest.builder().bucket(BUCKET).build());
    }
  }

  @Autowired MockMvc mockMvc;
  @Autowired SpringDataAuditLogRepository auditRepo;

  @Test
  void auditRowIsPersistedAfterRequest() throws Exception {
    long before = auditRepo.count();

    mockMvc
        .perform(post("/v1/jobs/merge").contentType("application/json").content("{\"fileCount\":2}").with(jwt()))
        .andExpect(status().isAccepted());

    await()
        .atMost(Duration.ofSeconds(1))
        .untilAsserted(() -> assertThat(auditRepo.count()).isGreaterThan(before));
  }
}
