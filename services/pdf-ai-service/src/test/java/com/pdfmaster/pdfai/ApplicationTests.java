package com.pdfmaster.pdfai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdfmaster.pdfai.adapter.out.persistence.DocumentChunkEntity;
import com.pdfmaster.pdfai.adapter.out.persistence.SpringDataDocumentChunkRepository;
import com.pdfmaster.pdfai.adapter.out.s3.S3ObjectStore;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
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

  // LocalStack S3 has no Spring Boot ServiceConnectionDetailsFactory — wire props manually below.
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
  @Autowired SpringDataDocumentChunkRepository chunkRepository;
  @Autowired RabbitTemplate rabbitTemplate;
  @Autowired ObjectMapper objectMapper;

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
                    .content(body)
                    .with(jwt()))
            .andExpect(MockMvcResultMatchers.status().isAccepted())
            .andReturn();
    String response = result.getResponse().getContentAsString();
    var tree = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
    assertThat(tree.get("uploadUrls")).hasSize(1);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/v1/jobs/" + tree.get("jobId").asText()).with(jwt()))
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
                .content(body)
                .with(jwt()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void unauthenticatedPostReturns401() throws Exception {
    String body = "{\"operation\":\"SUMMARIZE\"}";
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/jobs/ai")
                .contentType("application/json")
                .content(body))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  void unauthenticatedGetReturns401() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/v1/jobs/00000000-0000-0000-0000-000000000000"))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  void actuatorHealthIsPublic() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/actuator/health"))
        .andExpect(MockMvcResultMatchers.status().isOk());
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

  /**
   * Full RAG pipeline integration test using stub clients (no real Anthropic/OpenAI calls).
   *
   * <p>Flow: create job → upload minimal PDF to S3 → publish index event → poll until SUCCEEDED →
   * POST /v1/ai/summarize → assert non-empty stub response.
   */
  @Test
  void aiSummarizeUsesStubLlm() throws Exception {
    // 1. Create an AI job
    String createBody = "{\"operation\":\"SUMMARIZE\"}";
    var createResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/v1/jobs/ai")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createBody)
                    .with(jwt()))
            .andExpect(MockMvcResultMatchers.status().isAccepted())
            .andReturn();
    var tree = objectMapper.readTree(createResult.getResponse().getContentAsString());
    String jobId = tree.get("jobId").asText();
    String s3Key = tree.get("uploadUrls").get(0).get("key").asText();

    // 2. Upload a minimal valid PDF to LocalStack S3
    byte[] minimalPdf = minimalPdfBytes();
    objectStore.upload(s3Key, minimalPdf, "application/pdf", Map.of());

    // 3. Publish the index event directly via RabbitTemplate
    Map<String, Object> indexPayload =
        Map.of(
            "jobId", jobId,
            "inputKeys", List.of(s3Key),
            "options", Map.of("operation", "SUMMARIZE"));
    rabbitTemplate.convertAndSend("pdf.jobs", "pdf.ai.index.requested", indexPayload);

    // 4. Poll job status until SUCCEEDED (up to 20s)
    long deadline = System.currentTimeMillis() + 20_000;
    String status = "QUEUED";
    while (!status.equals("SUCCEEDED") && !status.equals("FAILED") && System.currentTimeMillis() < deadline) {
      Thread.sleep(500);
      var statusResult =
          mockMvc
              .perform(MockMvcRequestBuilders.get("/v1/jobs/" + jobId).with(jwt()))
              .andReturn();
      var statusTree = objectMapper.readTree(statusResult.getResponse().getContentAsString());
      status = statusTree.get("status").asText();
    }
    assertThat(status).isEqualTo("SUCCEEDED");

    // 5. POST to /v1/ai/summarize and assert non-empty response from StubLlmClient
    String summarizeBody = "{\"jobId\":\"" + jobId + "\",\"length\":\"medium\"}";
    var summarizeResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/v1/ai/summarize")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(summarizeBody)
                    .with(jwt()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    var summarizeTree = objectMapper.readTree(summarizeResult.getResponse().getContentAsString());
    String summary = summarizeTree.get("summary").asText();
    assertThat(summary).isNotBlank();
  }

  /** Produce a 3-page minimal valid PDF in memory using PDFBox 3.x API. */
  private static byte[] minimalPdfBytes() throws Exception {
    try (org.apache.pdfbox.pdmodel.PDDocument doc = new org.apache.pdfbox.pdmodel.PDDocument()) {
      org.apache.pdfbox.pdmodel.font.PDFont font =
          new org.apache.pdfbox.pdmodel.font.PDType1Font(
              org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.HELVETICA);
      for (int i = 1; i <= 3; i++) {
        org.apache.pdfbox.pdmodel.PDPage page =
            new org.apache.pdfbox.pdmodel.PDPage(
                org.apache.pdfbox.pdmodel.common.PDRectangle.A4);
        doc.addPage(page);
        try (org.apache.pdfbox.pdmodel.PDPageContentStream cs =
            new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page)) {
          cs.beginText();
          cs.setFont(font, 12);
          cs.newLineAtOffset(50, 700);
          cs.showText("This is page " + i + " of the test document. It contains sample text.");
          cs.endText();
        }
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      doc.save(out);
      return out.toByteArray();
    }
  }
}
