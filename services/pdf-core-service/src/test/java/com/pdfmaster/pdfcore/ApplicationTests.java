package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pdfmaster.pdfcore.adapter.out.s3.S3ObjectStore;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
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

  // LocalStack S3 has no Spring Boot ServiceConnectionDetailsFactory — wire via @DynamicPropertySource.
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

  @Autowired MockMvc mockMvc;
  @Autowired S3ObjectStore objectStore;

  @Test
  void contextLoads() {
    assertThat(objectStore.bucket()).isEqualTo(BUCKET);
  }

  @Test
  void createJobReturnsPresignedUrls() throws Exception {
    String body = "{\"fileCount\":3}";
    var result =
        mockMvc
            .perform(
                post("/v1/jobs/merge")
                    .contentType("application/json")
                    .content(body)
                    .with(jwt()))
            .andExpect(
                org.springframework.test.web.servlet.result.MockMvcResultMatchers.status()
                    .isAccepted())
            .andReturn();
    String response = result.getResponse().getContentAsString();
    assertThat(response).contains("uploadUrls").contains("jobId");

    com.fasterxml.jackson.databind.JsonNode tree =
        new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
    String jobId = tree.get("jobId").asText();
    assertThat(tree.get("uploadUrls")).hasSize(3);
    assertThat(tree.get("uploadUrls").get(0).get("url").asText()).startsWith("http");

    mockMvc
        .perform(get("/v1/jobs/" + jobId).with(jwt()))
        .andExpect(
            org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
        .andExpect(
            org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.status")
                .value("QUEUED"));
  }

  @Test
  void unknownJobReturns404() throws Exception {
    mockMvc
        .perform(get("/v1/jobs/00000000-0000-0000-0000-000000000000").with(jwt()))
        .andExpect(
            org.springframework.test.web.servlet.result.MockMvcResultMatchers.status()
                .isNotFound());
  }

  @Test
  void unauthenticatedPostReturns401() throws Exception {
    mockMvc
        .perform(post("/v1/jobs/merge").contentType("application/json").content("{\"fileCount\":1}"))
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

  // Suppress unused warning — referenced via class loader for testcontainers static init order.
  @SuppressWarnings("unused")
  private static List<String> ensureContainersBound() {
    return List.of(RABBIT.getContainerName(), LOCALSTACK.getContainerName());
  }
}
