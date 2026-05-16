package com.pdfmaster.pdfconvert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pdfmaster.pdfconvert.adapter.out.s3.S3ObjectStore;
import com.pdfmaster.pdfconvert.application.port.out.DocumentConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ApplicationTests {

  static final String BUCKET = "pdfmaster-test";

  @Container @ServiceConnection
  static final RabbitMQContainer RABBIT =
      new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-management"));

  // LocalStack S3 has no ServiceConnectionDetailsFactory — wire via @DynamicPropertySource below.
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
    registry.add("libreoffice.binary", () -> "/bin/true");
    registry.add("libreoffice.timeout-seconds", () -> "5");
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

  /** LibreOffice CLI isn't available in CI — stub it to avoid invoking the real binary. */
  @MockitoBean DocumentConverter documentConverter;

  @Autowired MockMvc mockMvc;
  @Autowired S3ObjectStore objectStore;

  @Test
  void contextLoads() {
    when(documentConverter.convert(any(), any(), any()))
        .thenReturn(
            new DocumentConverter.ConversionResult(new byte[] {1, 2, 3}, "application/pdf"));
    assertThat(objectStore.bucket()).isEqualTo(BUCKET);
  }

  @Test
  void createJobReturnsPresignedUrl() throws Exception {
    String body = "{\"from\":\"docx\",\"to\":\"pdf\"}";
    var result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/v1/jobs/convert")
                    .contentType("application/json")
                    .content(body)
                    .with(jwt()))
            .andExpect(MockMvcResultMatchers.status().isAccepted())
            .andReturn();
    String response = result.getResponse().getContentAsString();
    assertThat(response).contains("uploadUrls").contains("jobId");
    var tree = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
    assertThat(tree.get("uploadUrls")).hasSize(1);
    assertThat(tree.get("uploadUrls").get(0).get("url").asText()).startsWith("http");

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/v1/jobs/" + tree.get("jobId").asText()).with(jwt()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("QUEUED"));
  }

  @Test
  void rejectsSameFromTo() throws Exception {
    String body = "{\"from\":\"pdf\",\"to\":\"pdf\"}";
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/jobs/convert")
                .contentType("application/json")
                .content(body)
                .with(jwt()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void unauthenticatedPostReturns401() throws Exception {
    String body = "{\"from\":\"docx\",\"to\":\"pdf\"}";
    mockMvc
        .perform(
            post("/v1/jobs/convert").contentType("application/json").content(body))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void unauthenticatedGetReturns401() throws Exception {
    mockMvc
        .perform(get("/v1/jobs/00000000-0000-0000-0000-000000000000"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void actuatorHealthIsPublic() throws Exception {
    mockMvc.perform(get("/actuator/health")).andExpect(status().isOk());
  }
}
