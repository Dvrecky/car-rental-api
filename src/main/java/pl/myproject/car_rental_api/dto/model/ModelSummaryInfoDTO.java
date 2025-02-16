package pl.myproject.car_rental_api.dto.model;

import pl.myproject.car_rental_api.dto.engine.EngineSummaryInfoDTO;
import pl.myproject.car_rental_api.dto.gearbox.GearboxSummaryInfoDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ModelSummaryInfoDTO(
        String name,
        String type,
        LocalDate productionYear,
        String brand,
        String brandCountry,
        String color,
        String typeOfDrive,
        int numberOfDoors,
        String bodyType,
        int numberOfSeats,
        int weight,
        BigDecimal accelerationTime,
        String description,
        EngineSummaryInfoDTO engine,
        GearboxSummaryInfoDTO gearbox
) {}
