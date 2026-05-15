package com.pdfmaster.pdfcore.config;

import com.pdfmaster.pdfcore.adapter.out.s3.S3ObjectStore;
import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/** Wires {@link S3Client} and {@link S3Presigner} from {@link AwsS3Properties}. */
@Configuration
public class AwsS3Config {

  @Bean
  S3Client s3Client(AwsS3Properties properties) {
    var builder = S3Client.builder().region(Region.of(properties.region()));
    if (hasOverride(properties)) {
      builder
          .endpointOverride(URI.create(properties.endpoint()))
          .credentialsProvider(staticCreds(properties))
          .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build());
    } else {
      builder.credentialsProvider(DefaultCredentialsProvider.create());
    }
    return builder.build();
  }

  @Bean
  S3Presigner s3Presigner(AwsS3Properties properties) {
    var builder = S3Presigner.builder().region(Region.of(properties.region()));
    if (hasOverride(properties)) {
      builder
          .endpointOverride(URI.create(properties.endpoint()))
          .credentialsProvider(staticCreds(properties))
          .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build());
    } else {
      builder.credentialsProvider(DefaultCredentialsProvider.create());
    }
    return builder.build();
  }

  @Bean
  S3ObjectStore s3ObjectStore(S3Client client, S3Presigner presigner, AwsS3Properties properties) {
    return new S3ObjectStore(client, presigner, properties.bucket());
  }

  private static boolean hasOverride(AwsS3Properties properties) {
    return properties.endpoint() != null && !properties.endpoint().isBlank();
  }

  private static StaticCredentialsProvider staticCreds(AwsS3Properties properties) {
    String access = properties.accessKey() == null ? "test" : properties.accessKey();
    String secret = properties.secretKey() == null ? "test" : properties.secretKey();
    return StaticCredentialsProvider.create(AwsBasicCredentials.create(access, secret));
  }
}
