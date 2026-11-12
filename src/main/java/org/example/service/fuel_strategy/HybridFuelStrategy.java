package org.example.service.fuel_strategy;

import org.example.entity.enums.FuelType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HybridFuelStrategy implements FuelStrategy {

    @Override
    public FuelType getFuelType() {
        return FuelType.HYBRID;
    }

    @Override
    public BigDecimal calculateBaseFee(int year) {
        return BigDecimal.valueOf(100);
    }
}
