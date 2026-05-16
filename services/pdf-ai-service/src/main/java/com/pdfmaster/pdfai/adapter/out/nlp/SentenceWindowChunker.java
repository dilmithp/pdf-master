package com.pdfmaster.pdfai.adapter.out.nlp;

import com.pdfmaster.pdfai.application.port.out.Chunker;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/**
 * Sentence-aware sliding window chunker. Splits text on sentence boundaries (. ! ? followed by
 * whitespace or end-of-string), then groups sentences into chunks of at most {@code maxTokens}
 * tokens with {@code overlap} token overlap. Token count is approximated as {@code text.length() /
 * 4}.
 */
@Component
public class SentenceWindowChunker implements Chunker {

  /** Sentence boundary: end of sentence punctuation followed by whitespace or string end. */
  private static final Pattern SENTENCE_BOUNDARY =
      Pattern.compile("(?<=[.!?])(?=\\s+|$)", Pattern.MULTILINE);

  @Override
  public List<TextChunk> chunk(String text, int maxTokens, int overlap) {
    if (text == null || text.isBlank()) {
      return List.of();
    }
    List<String> sentences = splitIntoSentences(text);
    return buildChunks(sentences, maxTokens, overlap);
  }

  private static List<String> splitIntoSentences(String text) {
    String[] parts = SENTENCE_BOUNDARY.split(text);
    List<String> sentences = new ArrayList<>();
    for (String part : parts) {
      String trimmed = part.strip();
      if (!trimmed.isEmpty()) {
        sentences.add(trimmed);
      }
    }
    return sentences;
  }

  private static List<TextChunk> buildChunks(
      List<String> sentences, int maxTokens, int overlap) {
    List<TextChunk> chunks = new ArrayList<>();
    int i = 0;
    int chunkIndex = 0;
    while (i < sentences.size()) {
      // Accumulate sentences until the chunk is full
      List<String> window = new ArrayList<>();
      int tokens = 0;
      int j = i;
      while (j < sentences.size()) {
        int sentenceTokens = approxTokens(sentences.get(j));
        if (!window.isEmpty() && tokens + sentenceTokens > maxTokens) {
          break;
        }
        window.add(sentences.get(j));
        tokens += sentenceTokens;
        j++;
      }
      // Safety: if a single sentence exceeds maxTokens, force-include it
      if (window.isEmpty()) {
        window.add(sentences.get(i));
        j = i + 1;
      }
      chunks.add(new TextChunk(chunkIndex++, String.join(" ", window)));
      // Advance by as many sentences as take up (tokens - overlap) tokens from the front
      int overlapTokens = 0;
      int advance = window.size();
      for (int k = window.size() - 1; k >= 0; k--) {
        overlapTokens += approxTokens(window.get(k));
        if (overlapTokens >= overlap) {
          advance = k;
          break;
        }
      }
      i += Math.max(1, advance);
    }
    return chunks;
  }

  /** Approximate token count: 1 token ~ 4 characters. */
  static int approxTokens(String text) {
    return Math.max(1, (text.length() + 3) / 4);
  }
}
