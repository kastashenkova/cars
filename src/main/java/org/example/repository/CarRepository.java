package org.example.repository;

import org.example.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<CarEntity, String> {
    boolean existsByVehicleIdentificationNumber(String vehicleIdentificationNumber);

    boolean existsByLicensePlate(String licensePlate);
}
