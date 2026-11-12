package org.example.service.fuel_strategy;

import org.example.entity.enums.FuelType;

import java.math.BigDecimal;

public interface FuelStrategy {
    FuelType getFuelType();
    BigDecimal calculateBaseFee(int year);
}
