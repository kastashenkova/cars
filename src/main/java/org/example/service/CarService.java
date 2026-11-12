package org.example.service;

import java.math.BigDecimal;
import java.util.List;
import org.example.dto.CarRequestDto;
import org.example.dto.CarResponseDto;
import org.example.dto.UpdateCarStatusRequestDto;

public interface CarService {
    CarResponseDto createCar(CarRequestDto carRequestDto);
    CarResponseDto getCar(String id);
    List<CarResponseDto> getCars();
    CarResponseDto updateCarStatus(String id, UpdateCarStatusRequestDto statusRequestDto);
    void deleteCar(String id);
    BigDecimal calculateRentalSurchargeInInternationalCurrency(String carId, String currency);
}
