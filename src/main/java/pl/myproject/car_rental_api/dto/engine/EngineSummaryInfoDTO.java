package pl.myproject.car_rental_api.dto.engine;

import java.math.BigDecimal;

public record EngineSummaryInfoDTO(
        BigDecimal capacity,
        int horsepower,
        int torque,
        String fuelType,
        String cylinderConfiguration,
        String engineType
) {

}