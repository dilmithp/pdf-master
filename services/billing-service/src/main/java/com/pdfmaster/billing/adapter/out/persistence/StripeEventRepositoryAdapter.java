package com.pdfmaster.billing.adapter.out.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdfmaster.billing.application.StripeEventRepository;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** JPA-backed implementation of the {@link StripeEventRepository} outbound port. */
@Repository
public class StripeEventRepositoryAdapter implements StripeEventRepository {

  private static final Logger log = LoggerFactory.getLogger(StripeEventRepositoryAdapter.class);

  private final SpringDataStripeEventRepository jpa;
  private final ObjectMapper objectMapper;

  public StripeEventRepositoryAdapter(
      SpringDataStripeEventRepository jpa, ObjectMapper objectMapper) {
    this.jpa = jpa;
    this.objectMapper = objectMapper;
  }

  @Override
  @Transactional
  public boolean recordIfNew(String eventId, String eventType, String rawPayload) {
    if (jpa.existsById(eventId)) {
      return false;
    }
    try {
      JsonNode payloadNode = objectMapper.readTree(rawPayload);
      jpa.save(new StripeEventEntity(eventId, eventType, payloadNode, Instant.now()));
      return true;
    } catch (DataIntegrityViolationException e) {
      // Race condition: two threads passed existsById simultaneously; treat as duplicate.
      log.debug("Concurrent duplicate Stripe event id={} — treating as replay", eventId);
      return false;
    } catch (Exception e) {
      throw new IllegalStateException("Failed to persist Stripe event id=" + eventId, e);
    }
  }
}
