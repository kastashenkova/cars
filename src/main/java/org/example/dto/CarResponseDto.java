package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.entity.enums.CarStatus;
import org.example.entity.enums.FuelType;
import org.example.entity.enums.Transmission;

import java.math.BigDecimal;

public record CarResponseDto(@JsonProperty("car_id")
                             String carId,
                             @JsonProperty("owner_email")
                             String ownerEmail,
                             @JsonProperty("vehicle_identification_number")
                             String vehicleIdentificationNumber,
                             String model,
                             String color,
                             @JsonProperty("car_year")
                             int carYear,
                             @JsonProperty("license_plate")
                             String licensePlate,
                             @JsonProperty("fuel_type")
                             FuelType fuelType,
                             Transmission transmission,
                             @JsonProperty("base_fee")
                             BigDecimal baseFee,
                             @JsonProperty("rental_surcharge")
                             BigDecimal rentalSurcharge,
                             @JsonProperty("car_status")
                             CarStatus carStatus) {
}
