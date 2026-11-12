package org.example.health;

import java.io.File;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class StorageHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        File root = new File(".");
        long freeBytes = root.getFreeSpace();
        long totalBytes = root.getTotalSpace();

        // Поріг 100 МБ для перевірки
        long thresholdBytes = 100 * 1024 * 1024;

        if (freeBytes < thresholdBytes) {
            return Health.down()
                    .withDetail("storageType", "Local File System")
                    .withDetail("freeSpaceBytes", freeBytes)
                    .withDetail("totalSpaceBytes", totalBytes)
                    .withDetail("errorMessage", "Критично малий обсяг вільного дискового простору.")
                    .build();
        }

        return Health.up()
                .withDetail("storageType", "Local File System")
                .withDetail("freeSpaceBytes", freeBytes)
                .withDetail("totalSpaceBytes", totalBytes)
                .withDetail("statusDescription", "Дисковий простір доступний для запису")
                .build();
    }
}

