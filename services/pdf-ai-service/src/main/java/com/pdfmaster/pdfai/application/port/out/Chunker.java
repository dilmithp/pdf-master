package com.pdfmaster.pdfai.application.port.out;

import java.util.List;

/**
 * Outbound port for splitting a long text string into overlapping chunks. Implementation: {@code
 * SentenceWindowChunker}.
 */
public interface Chunker {

  /**
   * Split {@code text} into chunks of at most {@code maxTokens} tokens with {@code overlap} token
   * overlap between consecutive chunks. Token count is approximated as {@code text.length() / 4}.
   *
   * @param text the full document text
   * @param maxTokens target maximum tokens per chunk (hard cap)
   * @param overlap token overlap between successive chunks
   * @return ordered list of text chunks; never null, may be empty if {@code text} is blank
   */
  List<TextChunk> chunk(String text, int maxTokens, int overlap);

  /** A single text chunk with its zero-based sequence index. */
  record TextChunk(int index, String content) {}
}
