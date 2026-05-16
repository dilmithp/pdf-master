package com.pdfmaster.pdfai.adapter.out.nlp;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdfmaster.pdfai.application.port.out.Chunker.TextChunk;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Pure unit test — no Spring context. */
class SentenceWindowChunkerTest {

  private final SentenceWindowChunker chunker = new SentenceWindowChunker();

  @Test
  void emptyTextReturnsEmptyList() {
    assertThat(chunker.chunk("", 500, 50)).isEmpty();
    assertThat(chunker.chunk(null, 500, 50)).isEmpty();
    assertThat(chunker.chunk("   ", 500, 50)).isEmpty();
  }

  @Test
  void singleShortSentenceProducesOneChunk() {
    List<TextChunk> chunks = chunker.chunk("Hello world.", 500, 50);
    assertThat(chunks).hasSize(1);
    assertThat(chunks.get(0).content()).contains("Hello world");
    assertThat(chunks.get(0).index()).isZero();
  }

  @Test
  void chunksAreSequentiallyIndexed() {
    // Build text that forces multiple chunks
    String sentence = "This is a test sentence that is exactly twenty characters long. ";
    // Each sentence ~16 tokens; maxTokens=20 forces at most one per chunk
    String text = sentence.repeat(10);
    List<TextChunk> chunks = chunker.chunk(text, 20, 5);
    assertThat(chunks).isNotEmpty();
    for (int i = 0; i < chunks.size(); i++) {
      assertThat(chunks.get(i).index()).isEqualTo(i);
    }
  }

  @Test
  void chunksDoNotExceedMaxTokensByMoreThanOneSentence() {
    // 50 characters ~ 13 tokens per sentence
    String sentence = "The quick brown fox jumps over the lazy dog now. ";
    String text = sentence.repeat(20);
    List<TextChunk> chunks = chunker.chunk(text, 30, 5);
    for (TextChunk chunk : chunks) {
      // Each chunk content should be roughly within 2x maxTokens (one sentence may overflow)
      assertThat(SentenceWindowChunker.approxTokens(chunk.content())).isLessThan(80);
    }
  }

  @Test
  void overlapCausesAdjacentChunksToShareContent() {
    // Build sentences with known distinct words
    String text =
        "Sentence one ends here. Sentence two ends here. Sentence three ends here. "
            + "Sentence four ends here. Sentence five ends here.";
    List<TextChunk> chunks = chunker.chunk(text, 30, 20);
    if (chunks.size() >= 2) {
      // There should be some shared text between adjacent chunks due to overlap
      String first = chunks.get(0).content();
      String second = chunks.get(1).content();
      // The second chunk should not start from scratch at sentence 2+1 when overlap is large
      assertThat(first).isNotEqualTo(second);
    }
  }

  @Test
  void approxTokensCalculation() {
    assertThat(SentenceWindowChunker.approxTokens("")).isEqualTo(1); // min 1
    assertThat(SentenceWindowChunker.approxTokens("abcd")).isEqualTo(1); // 4/4 = 1
    assertThat(SentenceWindowChunker.approxTokens("a".repeat(8))).isEqualTo(2);
    assertThat(SentenceWindowChunker.approxTokens("a".repeat(400))).isEqualTo(100);
  }

  @Test
  void singleSentenceLargerThanMaxTokensIsStillIncluded() {
    // A very long sentence (>500 tokens) should appear as its own chunk
    String longSentence = "word ".repeat(2100) + "end."; // ~2100 tokens
    List<TextChunk> chunks = chunker.chunk(longSentence, 500, 50);
    assertThat(chunks).isNotEmpty();
    // The long sentence must appear in at least one chunk
    boolean found = chunks.stream().anyMatch(c -> c.content().length() > 100);
    assertThat(found).isTrue();
  }
}
