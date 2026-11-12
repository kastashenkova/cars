package org.example.service.transmission_strategy;

import java.math.BigDecimal;
import org.example.entity.enums.Transmission;

public interface TransmissionStrategy {
    Transmission getTransmission();
    BigDecimal calculateRentalSurcharge(BigDecimal basePrice);
}
