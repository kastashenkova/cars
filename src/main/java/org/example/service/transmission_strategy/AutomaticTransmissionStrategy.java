package org.example.service.transmission_strategy;

import java.math.BigDecimal;
import org.example.entity.enums.Transmission;
import org.springframework.stereotype.Component;

@Component
public class AutomaticTransmissionStrategy implements TransmissionStrategy {
    @Override
    public Transmission getTransmission() {
        return Transmission.AUTOMATIC;
    }

    @Override
    public BigDecimal calculateRentalSurcharge(BigDecimal basePrice) {
        return basePrice.multiply(BigDecimal.valueOf(0.15));
    }
}
