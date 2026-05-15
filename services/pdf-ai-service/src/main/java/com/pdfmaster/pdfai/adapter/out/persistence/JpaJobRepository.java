package com.pdfmaster.pdfai.adapter.out.persistence;

import com.pdfmaster.pdfai.application.port.out.JobRepository;
import com.pdfmaster.pdfai.domain.JobId;
import com.pdfmaster.pdfai.domain.JobRecord;
import com.pdfmaster.pdfai.domain.JobStatus;
import java.time.Clock;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Postgres-backed implementation of {@link JobRepository}. */
@Component
public class JpaJobRepository implements JobRepository {

  private final SpringDataJobRepository delegate;
  private final Clock clock;

  public JpaJobRepository(SpringDataJobRepository delegate, Clock clock) {
    this.delegate = delegate;
    this.clock = clock;
  }

  @Override
  @Transactional
  public JobRecord save(JobRecord job) {
    JobEntity entity =
        new JobEntity(
            job.id().value(),
            job.op(),
            job.status(),
            job.inputKeys(),
            job.outputKey().orElse(null),
            job.error().orElse(null),
            job.createdAt(),
            job.updatedAt());
    delegate.save(entity);
    return job;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<JobRecord> findById(JobId id) {
    return delegate.findById(id.value()).map(JpaJobRepository::toDomain);
  }

  @Override
  @Transactional
  public JobRecord markStatus(JobId id, JobStatus next) {
    JobEntity entity =
        delegate
            .findById(id.value())
            .orElseThrow(() -> new NoSuchElementException("Unknown jobId: " + id));
    entity.setStatus(next);
    entity.setUpdatedAt(clock.instant());
    delegate.save(entity);
    return toDomain(entity);
  }

  private static JobRecord toDomain(JobEntity entity) {
    return new JobRecord(
        new JobId(entity.getId()),
        entity.getOp(),
        entity.getStatus(),
        entity.getInputKeys(),
        Optional.ofNullable(entity.getOutputKey()),
        Optional.ofNullable(entity.getErrorMessage()),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }
}
