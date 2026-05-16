package com.pdfmaster.pdfai.adapter.out.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.pdfmaster.pdfai.application.port.out.EmbeddingClient;
import com.pdfmaster.pdfai.config.AiProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

/**
 * Production {@link EmbeddingClient} using the OpenAI embeddings API via {@link RestClient}.
 * Batches up to 100 inputs per request as per OpenAI's limit.
 */
public class OpenAiEmbeddingClient implements EmbeddingClient {

  private static final Logger LOG = LoggerFactory.getLogger(OpenAiEmbeddingClient.class);
  private static final String BASE_URL = "https://api.openai.com";
  private static final int BATCH_SIZE = 100;

  private final RestClient restClient;
  private final AiProperties props;

  public OpenAiEmbeddingClient(RestClient.Builder builder, AiProperties props) {
    this.props = props;
    this.restClient =
        builder
            .baseUrl(BASE_URL)
            .defaultHeader("Authorization", "Bearer " + props.openaiApiKey())
            .defaultHeader("content-type", "application/json")
            .build();
  }

  @Override
  public float[] embed(String text) {
    List<float[]> result = embedBatch(List.of(text));
    return result.isEmpty() ? new float[0] : result.get(0);
  }

  @Override
  public List<float[]> embedBatch(List<String> texts) {
    if (texts.isEmpty()) {
      return List.of();
    }
    List<float[]> all = new ArrayList<>(texts.size());
    for (int start = 0; start < texts.size(); start += BATCH_SIZE) {
      List<String> batch = texts.subList(start, Math.min(start + BATCH_SIZE, texts.size()));
      all.addAll(callApi(batch));
    }
    return all;
  }

  private List<float[]> callApi(List<String> inputs) {
    Map<String, Object> body =
        Map.of(
            "model", props.openaiEmbedModel(),
            "input", inputs,
            "encoding_format", "float");
    LOG.debug("OpenAI embed call: inputs={}", inputs.size());
    JsonNode response =
        restClient.post().uri("/v1/embeddings").body(body).retrieve().body(JsonNode.class);
    return parseEmbeddings(response);
  }

  private static List<float[]> parseEmbeddings(JsonNode response) {
    if (response == null) {
      return List.of();
    }
    JsonNode data = response.get("data");
    if (data == null || !data.isArray()) {
      return List.of();
    }
    List<float[]> result = new ArrayList<>(data.size());
    for (JsonNode item : data) {
      JsonNode embNode = item.get("embedding");
      if (embNode == null || !embNode.isArray()) {
        continue;
      }
      float[] vec = new float[embNode.size()];
      for (int i = 0; i < embNode.size(); i++) {
        vec[i] = (float) embNode.get(i).asDouble();
      }
      result.add(vec);
    }
    return result;
  }
}
