package com.pdfmaster.pdfconvert.adapter.out.job;

import com.pdfmaster.pdfconvert.application.port.out.JobRepository;
import com.pdfmaster.pdfconvert.domain.JobId;
import com.pdfmaster.pdfconvert.domain.JobRecord;
import com.pdfmaster.pdfconvert.domain.JobStatus;
import java.time.Clock;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Component;

/** Scaffolding-tier in-memory job store. Production moves to Redis. */
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
