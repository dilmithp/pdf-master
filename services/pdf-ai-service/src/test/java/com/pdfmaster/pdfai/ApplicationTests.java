package com.pdfmaster.pdfai;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdfmaster.pdfai.adapter.out.persistence.DocumentChunkEntity;
import com.pdfmaster.pdfai.adapter.out.persistence.SpringDataDocumentChunkRepository;
import com.pdfmaster.pdfai.adapter.out.s3.S3ObjectStore;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ApplicationTests {

  static final String BUCKET = "pdfmaster-test";

  /**
   * pgvector/pgvector ships pgvector pre-installed; the image is compatible with
   * PostgreSQLContainer.
   */
  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>(
              DockerImageName.parse("pgvector/pgvector:pg16").asCompatibleSubstituteFor("postgres"))
          .withDatabaseName("pdfmaster")
          .withUsername("pdfmaster")
          .withPassword("pdfmaster");

  @Container @ServiceConnection
  static final RabbitMQContainer RABBIT =
      new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-management"));

  @Container @ServiceConnection
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
  @Autowired SpringDataDocumentChunkRepository chunkRepository;

  @Test
  void contextLoads() {
    assertThat(objectStore.bucket()).isEqualTo(BUCKET);
  }

  @Test
  void createJobReturnsPresignedUrl() throws Exception {
    String body = "{\"operation\":\"SUMMARIZE\"}";
    var result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/v1/jobs/ai")
                    .contentType("application/json")
                    .content(body))
            .andExpect(MockMvcResultMatchers.status().isAccepted())
            .andReturn();
    String response = result.getResponse().getContentAsString();
    var tree = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
    assertThat(tree.get("uploadUrls")).hasSize(1);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/v1/jobs/" + tree.get("jobId").asText()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("QUEUED"));
  }

  @Test
  void rejectsUnknownOperation() throws Exception {
    String body = "{\"operation\":\"NOPE\"}";
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/jobs/ai")
                .contentType("application/json")
                .content(body))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void documentChunkRoundTripsThroughPgvector() {
    float[] embedding = new float[DocumentChunkEntity.EMBEDDING_DIMENSION];
    for (int i = 0; i < embedding.length; i++) {
      embedding[i] = (float) Math.sin(i * 0.001);
    }
    UUID id = UUID.randomUUID();
    UUID jobId = UUID.randomUUID();
    DocumentChunkEntity saved =
        chunkRepository.save(new DocumentChunkEntity(id, jobId, "hello", embedding));
    assertThat(saved.getId()).isEqualTo(id);

    DocumentChunkEntity reloaded = chunkRepository.findById(id).orElseThrow();
    assertThat(reloaded.getJobId()).isEqualTo(jobId);
    assertThat(reloaded.getContent()).isEqualTo("hello");
    assertThat(reloaded.getEmbedding()).hasSize(DocumentChunkEntity.EMBEDDING_DIMENSION);
    assertThat(reloaded.getEmbedding()[0]).isEqualTo(embedding[0]);
  }
}
