package org.example.service.fuel_strategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.example.entity.enums.FuelType;
import org.springframework.stereotype.Component;

@Component
public class DieselFuelStrategy implements FuelStrategy {
    @Override
    public FuelType getFuelType() {
        return FuelType.DIESEL;
    }

    @Override
    public BigDecimal calculateBaseFee(int year) {
        int currentYear = LocalDate.now().getYear();
        int age = currentYear - year - 1;

        if (age <= 0) {
            age = 1;
        } else if (age > 15) {
            age = 15;
        }

        BigDecimal baseRateEur = BigDecimal.valueOf(75);

        return baseRateEur.multiply(BigDecimal.valueOf(age));
    }
}
