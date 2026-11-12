package org.example.dto;

import org.example.entity.enums.CarStatus;

public record UpdateCarStatusRequestDto(
        CarStatus carStatus
) {
}
