package org.example.service.fuel_strategy;

import org.example.entity.enums.FuelType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ElectricFuelStrategy implements FuelStrategy {

    @Override
    public FuelType getFuelType() {
        return FuelType.ELECTRIC;
    }

    @Override
    public BigDecimal calculateBaseFee(int year) {
        return BigDecimal.valueOf(50);
    }
}
