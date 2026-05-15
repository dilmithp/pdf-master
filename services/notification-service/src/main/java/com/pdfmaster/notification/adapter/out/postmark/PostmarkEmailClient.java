package com.pdfmaster.notification.adapter.out.postmark;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pdfmaster.notification.application.EmailClient;
import com.pdfmaster.notification.config.PostmarkProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

/** Postmark email sender backed by Spring's {@link RestClient}. */
@Component
@Profile("!test")
public class PostmarkEmailClient implements EmailClient {

  private final RestClient restClient;
  private final PostmarkProperties properties;

  public PostmarkEmailClient(RestClient.Builder builder, PostmarkProperties properties) {
    this.properties = properties;
    this.restClient =
        builder
            .baseUrl(properties.baseUrl())
            .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("X-Postmark-Server-Token", properties.serverToken())
            .build();
  }

  @Override
  public String send(String recipient, String subject, String body) {
    PostmarkRequest payload =
        new PostmarkRequest(properties.fromAddress(), recipient, subject, body);
    try {
      PostmarkResponse response =
          restClient
              .post()
              .uri("/email")
              .body(payload)
              .retrieve()
              .onStatus(
                  HttpStatusCode::isError,
                  (req, res) -> {
                    throw new PostmarkDeliveryException(
                        "Postmark returned status " + res.getStatusCode());
                  })
              .body(PostmarkResponse.class);
      if (response == null || response.messageId() == null) {
        throw new PostmarkDeliveryException("Postmark returned empty MessageID");
      }
      return response.messageId();
    } catch (RestClientResponseException e) {
      throw new PostmarkDeliveryException("Postmark request failed", e);
    }
  }

  /** Request body matching Postmark's /email contract. */
  public record PostmarkRequest(
      @JsonProperty("From") String from,
      @JsonProperty("To") String to,
      @JsonProperty("Subject") String subject,
      @JsonProperty("HtmlBody") String htmlBody) {}

  /** Subset of the Postmark /email response. */
  public record PostmarkResponse(
      @JsonProperty("MessageID") String messageId,
      @JsonProperty("ErrorCode") Integer errorCode,
      @JsonProperty("Message") String message) {}

  /** Thrown when Postmark refuses or fails the send. */
  public static class PostmarkDeliveryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PostmarkDeliveryException(String message) {
      super(message);
    }

    public PostmarkDeliveryException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
