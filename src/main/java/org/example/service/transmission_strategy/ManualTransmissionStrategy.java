package org.example.service.transmission_strategy;

import java.math.BigDecimal;
import org.example.entity.enums.Transmission;
import org.springframework.stereotype.Component;

@Component
public class ManualTransmissionStrategy implements TransmissionStrategy {
    @Override
    public Transmission getTransmission() {
        return Transmission.MANUAL;
    }

    @Override
    public BigDecimal calculateRentalSurcharge(BigDecimal basePrice) {
        return BigDecimal.ZERO;
    }
}
