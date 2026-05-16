package com.pdfmaster.pdfai.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pdfmaster.pdfai.application.port.out.Chunker;
import com.pdfmaster.pdfai.application.port.out.Chunker.TextChunk;
import com.pdfmaster.pdfai.application.port.out.DocumentChunkRepository;
import com.pdfmaster.pdfai.application.port.out.EmbeddingClient;
import com.pdfmaster.pdfai.application.port.out.TextExtractor;
import com.pdfmaster.pdfai.config.AiProperties;
import com.pdfmaster.pdfai.domain.DocumentChunk;
import com.pdfmaster.pdfai.domain.JobId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AiIndexServiceTest {

  @Mock TextExtractor textExtractor;
  @Mock Chunker chunker;
  @Mock EmbeddingClient embeddingClient;
  @Mock DocumentChunkRepository chunkRepository;

  private AiIndexService service;
  private static final AiProperties PROPS =
      new AiProperties("", "claude-opus-4-7", "", "text-embedding-3-small", 500, 50, 5);

  @BeforeEach
  void setUp() {
    service = new AiIndexService(textExtractor, chunker, embeddingClient, chunkRepository, PROPS);
  }

  @Test
  void indexOrchestrates_extract_chunk_embed_persist() {
    byte[] pdfBytes = "fake-pdf".getBytes();
    JobId jobId = JobId.random();
    String text = "Hello world. Second sentence here.";
    List<TextChunk> chunks = List.of(new TextChunk(0, "Hello world."), new TextChunk(1, "Second sentence here."));
    float[] emb0 = new float[1536];
    float[] emb1 = new float[1536];
    emb0[0] = 0.1f;
    emb1[0] = 0.2f;

    when(textExtractor.extract(pdfBytes)).thenReturn(text);
    when(chunker.chunk(text, 500, 50)).thenReturn(chunks);
    when(embeddingClient.embedBatch(List.of("Hello world.", "Second sentence here.")))
        .thenReturn(List.of(emb0, emb1));
    when(chunkRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    service.index(jobId, pdfBytes);

    verify(textExtractor).extract(pdfBytes);
    verify(chunker).chunk(text, 500, 50);
    verify(embeddingClient).embedBatch(List.of("Hello world.", "Second sentence here."));

    ArgumentCaptor<DocumentChunk> captor = ArgumentCaptor.forClass(DocumentChunk.class);
    verify(chunkRepository, times(2)).save(captor.capture());
    List<DocumentChunk> saved = captor.getAllValues();
    assertThat(saved).hasSize(2);
    assertThat(saved.get(0).jobId()).isEqualTo(jobId);
    assertThat(saved.get(0).content()).isEqualTo("Hello world.");
    assertThat(saved.get(1).content()).isEqualTo("Second sentence here.");
  }

  @Test
  void rejectsDocumentExceedingCharacterLimit() {
    byte[] pdfBytes = "fake-pdf".getBytes();
    JobId jobId = JobId.random();
    String hugeText = "x".repeat(500_001);

    when(textExtractor.extract(pdfBytes)).thenReturn(hugeText);

    assertThatThrownBy(() -> service.index(jobId, pdfBytes))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("exceeds maximum size");

    verify(chunker, never()).chunk(any(), anyInt(), anyInt());
    verify(embeddingClient, never()).embedBatch(anyList());
    verify(chunkRepository, never()).save(any());
  }

  @Test
  void emptyDocumentProducesNoChunks() {
    byte[] pdfBytes = "fake-pdf".getBytes();
    JobId jobId = JobId.random();

    when(textExtractor.extract(pdfBytes)).thenReturn("");
    when(chunker.chunk("", 500, 50)).thenReturn(List.of());
    when(embeddingClient.embedBatch(List.of())).thenReturn(List.of());

    service.index(jobId, pdfBytes);

    verify(chunkRepository, never()).save(any());
  }
}
