package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.entity.enums.FuelType;
import org.example.entity.enums.Transmission;
import org.example.validation.OnCreate;
import org.example.validation.OnUpdate;
import org.example.validation.ValidLicensePlate;

public record CarRequestDto(
        @NotNull(message = "{validation.carId.creation.not-null}", groups = {OnCreate.class, OnUpdate.class})
        String carId,

        @Email(message = "{validation.email.format}", groups = {OnCreate.class, OnUpdate.class})
        @NotBlank(message = "{validation.email.not-blank}", groups = {OnCreate.class, OnUpdate.class})
        String ownerEmail,

        @Size(min = 17, max = 17, message = "{validation.vehicleIdentificationNumber.size}", groups = {OnCreate.class, OnUpdate.class})
        String vehicleIdentificationNumber,

        @NotBlank(message = "{validation.model.not-blank}", groups = {OnCreate.class, OnUpdate.class})
        String model,

        @NotBlank(message = "{validation.color.not-blank}", groups = {OnCreate.class, OnUpdate.class})
        String color,

        @NotNull(message = "{validation.year.not-null}", groups = {OnCreate.class, OnUpdate.class})
        @Min(value = 1886, message = "{validation.year.min}", groups = {OnCreate.class, OnUpdate.class})
        @Max(
                value = 2026,
                message = "{validation.year.max}",
                groups = {OnCreate.class, OnUpdate.class}
        )
        Integer carYear,

        @ValidLicensePlate(groups = {OnCreate.class, OnUpdate.class})
        String licensePlate,

        FuelType fuelType,

        Transmission transmission
) {
}
