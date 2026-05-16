package com.pdfmaster.pdfai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Externalized configuration for the AI pipeline. Bound from the {@code ai.*} prefix. All keys
 * default to safe no-op values so the service starts without real API credentials.
 */
@ConfigurationProperties("ai")
public record AiProperties(
    String anthropicApiKey,
    String anthropicModel,
    String openaiApiKey,
    String openaiEmbedModel,
    Integer maxChunkTokens,
    Integer chunkOverlap,
    Integer topK) {

  public AiProperties {
    anthropicApiKey = anthropicApiKey == null ? "" : anthropicApiKey;
    anthropicModel = anthropicModel == null ? "claude-opus-4-7" : anthropicModel;
    openaiApiKey = openaiApiKey == null ? "" : openaiApiKey;
    openaiEmbedModel = openaiEmbedModel == null ? "text-embedding-3-small" : openaiEmbedModel;
    maxChunkTokens = maxChunkTokens == null ? 500 : maxChunkTokens;
    chunkOverlap = chunkOverlap == null ? 50 : chunkOverlap;
    topK = topK == null ? 5 : topK;
  }
}
