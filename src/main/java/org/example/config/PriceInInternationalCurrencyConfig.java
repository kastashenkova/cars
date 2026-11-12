package org.example.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriceInInternationalCurrencyConfig {

    @Bean("EUR")
    public Function<BigDecimal, BigDecimal> calculateRentalSurchargeInEuros() {
        return (BigDecimal price) -> {
            if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(
                        "Price must be non-negative"
                );
            }

            return price.divide(BigDecimal.valueOf(50.76), 2, RoundingMode.HALF_UP);
        };
    }

    @Bean("USD")
    public Function<BigDecimal, BigDecimal> calculateRentalSurchargeInDollars() {
        return (BigDecimal price) -> {
            if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(
                        "Price must be non-negative"
                );
            }

            return price.divide(BigDecimal.valueOf(45.55), 2, RoundingMode.HALF_UP);
        };
    }
}
