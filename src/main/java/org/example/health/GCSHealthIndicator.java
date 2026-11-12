package org.example.health;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class GCSHealthIndicator implements HealthIndicator {

    private final Storage storage;
    private final String bucketName;

    public GCSHealthIndicator(Storage storage,
                              @Value("${gcp.storage.bucket-name}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    @Override
    public Health health() {
        try {
            long start = System.currentTimeMillis();

            Bucket bucket = storage.get(bucketName);
            long latency = System.currentTimeMillis() - start;

            if (bucket != null && bucket.exists()) {
                return Health.up()
                        .withDetail("storageType", "Google Cloud Storage")
                        .withDetail("bucket", bucketName)
                        .withDetail("latencyMs", latency)
                        .withDetail("statusDescription", "GCS bucket is accessible")
                        .build();
            } else {
                return Health.down()
                        .withDetail("storageType", "Google Cloud Storage")
                        .withDetail("bucket", bucketName)
                        .withDetail("errorMessage", "Bucket not found or access denied")
                        .build();
            }
        } catch (Exception ex) {
            return Health.down(ex)
                    .withDetail("storageType", "Google Cloud Storage")
                    .withDetail("bucket", bucketName)
                    .build();
        }
    }
}
