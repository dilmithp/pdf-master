package com.pdfmaster.pdfocr.adapter.out.job;

import com.pdfmaster.pdfocr.application.port.out.JobRepository;
import com.pdfmaster.pdfocr.domain.JobId;
import com.pdfmaster.pdfocr.domain.JobRecord;
import com.pdfmaster.pdfocr.domain.JobStatus;
import java.time.Clock;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryJobRepository implements JobRepository {

  private final ConcurrentMap<JobId, JobRecord> store = new ConcurrentHashMap<>();
  private final Clock clock;

  public InMemoryJobRepository(Clock clock) {
    this.clock = clock;
  }

  @Override
  public JobRecord save(JobRecord job) {
    store.put(job.id(), job);
    return job;
  }

  @Override
  public Optional<JobRecord> findById(JobId id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public JobRecord markStatus(JobId id, JobStatus next) {
    JobRecord updated =
        store.computeIfPresent(id, (k, current) -> current.withStatus(next, clock.instant()));
    if (updated == null) {
      throw new NoSuchElementException("Unknown jobId: " + id);
    }
    return updated;
  }
}
