package org.example.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.example.aspect.AuditAction;
import org.example.dto.CarRequestDto;
import org.example.dto.UpdateCarStatusRequestDto;
import org.example.entity.CarEntity;
import org.example.dto.CarResponseDto;
import org.example.entity.enums.CarStatus;
import org.example.exception.CarNotFoundException;
import org.example.exception.DuplicateCarException;
import org.example.exception.InvalidCarStateException;
import org.example.exception.InvalidCurrencyException;
import org.example.repository.CarRepository;
import org.example.service.fuel_strategy.FuelStrategy;
import org.example.service.transmission_strategy.TransmissionStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final Map<String, FuelStrategy> fuelStrategyMap;
    private final Map<String, TransmissionStrategy> transmissionStrategyMap;
    private final Map<String, Function<BigDecimal, BigDecimal>> converters;

    public CarServiceImpl(CarRepository carRepository,
                          List <FuelStrategy> fuelStrategyList,
                          List <TransmissionStrategy> transmissionStrategyList,
                          Map<String, Function<BigDecimal, BigDecimal>> converters) {
        this.carRepository = carRepository;
        this.fuelStrategyMap = fuelStrategyList.stream()
                .collect(Collectors.toMap(
                        s -> s.getFuelType().toString(),
                        Function.identity()
                ));
        this.transmissionStrategyMap = transmissionStrategyList.stream()
                .collect(Collectors.toMap(
                        s -> s.getTransmission().toString(),
                        Function.identity()
                ));
        this.converters = converters;
    }

    @Override
    @AuditAction(action = "RENTAL_SURCHARGE_IN_CURRENCY")
    public BigDecimal calculateRentalSurchargeInInternationalCurrency(String carId, String currency) {
        CarEntity car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car does not exist with id: " + carId));

        Function<BigDecimal, BigDecimal> converter = converters.get(currency.toUpperCase());
        if (converter == null) {
            throw new InvalidCurrencyException("Unsupported currency: " + currency);
        }
        return converter.apply(car.getRentalSurcharge()).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    @AuditAction(action = "CREATE_CAR", logParameters = false)
    public CarResponseDto createCar(CarRequestDto carRequestDto) {
        String id = (carRequestDto.carId() != null && !carRequestDto.carId().isBlank())
                ? carRequestDto.carId()
                : UUID.randomUUID().toString();

        if (carRepository.existsById(id)) {
            throw new DuplicateCarException(
                    "Car with ID '" + id + "' already exists");
        }

        if (carRepository.existsByVehicleIdentificationNumber((
                carRequestDto.vehicleIdentificationNumber()))) {
            throw new DuplicateCarException(
                    "Car with vehicle identification number '"
                            + carRequestDto.vehicleIdentificationNumber() + "' already exists");
        }

        if (carRepository.existsByLicensePlate((
                carRequestDto.licensePlate()))) {
            throw new DuplicateCarException(
                    "Car with license plate '"
                            + carRequestDto.licensePlate() + "' already exists");
        }

        String fuelType = carRequestDto.fuelType().toString();
        FuelStrategy fuelStrategy = fuelStrategyMap.get(fuelType);
        if (fuelStrategy == null) {
            throw new InvalidCarStateException(
                    "Unsupported fuel type: " + carRequestDto.fuelType());
        }

        String transmission = carRequestDto.transmission().toString();
        TransmissionStrategy transmissionStrategy = transmissionStrategyMap.get(transmission);
        if (transmissionStrategy == null) {
            throw new InvalidCarStateException(
                    "Unsupported transmission type: " + carRequestDto.transmission());
        }

        BigDecimal targetBaseFee = fuelStrategy.calculateBaseFee(carRequestDto.carYear());
        BigDecimal targetRentalSurcharge = transmissionStrategy
                .calculateRentalSurcharge(targetBaseFee);

        CarEntity newCar = new CarEntity();
        newCar.setCarId(id)
                .setOwnerEmail(carRequestDto.ownerEmail())
                .setVehicleIdentificationNumber(carRequestDto.vehicleIdentificationNumber())
                .setLicensePlate(carRequestDto.licensePlate())
                .setModel(carRequestDto.model())
                .setColor(carRequestDto.color())
                .setCarYear(carRequestDto.carYear())
                .setFuelType(carRequestDto.fuelType())
                .setTransmission(carRequestDto.transmission())
                .setBaseFee(targetBaseFee)
                .setRentalSurcharge(targetRentalSurcharge)
                .setCarStatus(CarStatus.AVAILABLE);

        CarEntity savedCar = carRepository.save(newCar);

        return mapToResponse(savedCar);
    }

    @Override
    @AuditAction(action = "GET_CAR")
    public CarResponseDto getCar(String id) {
        CarEntity car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car does not exist with id: " + id));
        System.out.println(car);
        return mapToResponse(car);
    }

    @Override
    @AuditAction(action = "GET_CARS")
    public List<CarResponseDto> getCars() {
        List<CarEntity> cars = new ArrayList<>(carRepository.findAll());
        System.out.println(cars);

        return cars.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    @AuditAction(action = "UPDATE_STATUS", logParameters = false)
    public CarResponseDto updateCarStatus(String id, UpdateCarStatusRequestDto statusRequestDto) {
        CarEntity car = carRepository.findById(id)
                .orElseThrow(() ->
                        new CarNotFoundException(
                                "Car with id " + id + " not found"
                        ));

        CarStatus currentStatus = car.getCarStatus();
        CarStatus newStatus = statusRequestDto.carStatus();

        if (newStatus == null
                || !currentStatus.canTransitionTo(newStatus)) {
            throw new InvalidCarStateException(
                    "Illegal state transition for car " + id
                            + " from " + currentStatus
                            + " to " + newStatus
            );
        }

        car.setCarStatus(newStatus);

        return mapToResponse(car);
    }

    @Override
    @Transactional
    @AuditAction(action = "DELETE_CAR", logParameters = false)
    public void deleteCar(String id) {
        carRepository.findById(id)
                .orElseThrow(
                        () -> new CarNotFoundException("Car with id " + id + " not found"));
        carRepository.deleteById(id);
    }

    private CarResponseDto mapToResponse(CarEntity entity) {
        return new CarResponseDto(
                entity.getCarId(),
                entity.getOwnerEmail(),
                entity.getVehicleIdentificationNumber(),
                entity.getModel(),
                entity.getColor(),
                entity.getCarYear(),
                entity.getLicensePlate(),
                entity.getFuelType(),
                entity.getTransmission(),
                entity.getBaseFee(),
                entity.getRentalSurcharge(),
                entity.getCarStatus()
        );
    }
}
