
package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.entity.enums.CarStatus;
import org.example.entity.enums.FuelType;
import org.example.entity.enums.Transmission;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "cars")
public class CarEntity {
    @Id
    @Column(name = "car_id", nullable = false, length = 250)
    private String carId;

    @Column(name = "owner_email", nullable = false, length = 250)
    private String ownerEmail;

    @Column(
            name = "vehicle_identification_number",
            nullable = false,
            length = 17
    )
    private String vehicleIdentificationNumber;

    @Column(nullable = false, length = 250)
    private String model;

    @Column(nullable = false, length = 250)
    private String color;

    @Column(name = "car_year", nullable = false)
    private int carYear;

    @Column(name = "license_plate", nullable = false, length = 250)
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false, length = 250)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission", nullable = false, length = 250)
    private Transmission transmission;

    @Column(name = "base_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseFee;

    @Column(
            name = "rental_surcharge",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal rentalSurcharge;

    @Enumerated(EnumType.STRING)
    @Column(name = "car_status", nullable = false, length = 50)
    private CarStatus carStatus;
}
