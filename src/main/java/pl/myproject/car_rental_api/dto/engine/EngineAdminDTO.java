package pl.myproject.car_rental_api.dto.engine;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EngineAdminDTO {

    private int id;
    private BigDecimal capacity;
    private int horsepower;
    private int torque;
    private String fuelType;
    private String cylinderConfiguration;
    private String engineType;
}
