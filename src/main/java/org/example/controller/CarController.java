package org.example.controller;

import java.math.BigDecimal;
import java.util.List;
import org.example.dto.CarRequestDto;
import org.example.dto.CarResponseDto;
import org.example.dto.UpdateCarStatusRequestDto;
import org.example.service.CarService;
import org.example.validation.OnCreate;
import org.example.validation.OnUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/rental-fee-other-currency/{id}")
    public BigDecimal calculatePrice(@PathVariable String id,
                                     @RequestParam String currency) {
        return carService.calculateRentalSurchargeInInternationalCurrency(id, currency);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarResponseDto createCar(
            @Validated(OnCreate.class)
            @RequestBody CarRequestDto request) {
        return carService.createCar(request);
    }

    @GetMapping("/{id}")
    public CarResponseDto getCarById(@PathVariable String id) {
        return carService.getCar(id);
    }

    @GetMapping
    public List<CarResponseDto> getCars() {
        return carService.getCars();
    }

    @PutMapping("/{id}/status")
    public CarResponseDto updateCarStatus(
            @PathVariable String id,
            @Validated(OnUpdate.class)
            @RequestBody UpdateCarStatusRequestDto statusRequestDto) {
        return carService.updateCarStatus(id, statusRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable String id) {
        carService.deleteCar(id);
    }
}
