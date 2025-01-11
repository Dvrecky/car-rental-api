package pl.myproject.car_rental_api.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EngineDTO {

    private int id;
    private BigDecimal capacity;
    private int horsepower;
    private int torque;
    private String fuelType;
    private String cylinderConfiguration;
    private String engineType;
}
