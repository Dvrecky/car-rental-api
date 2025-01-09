package pl.myproject.car_rental_api.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EngineDTO {

    private int id;
    private double capacity;
    private int horsepower;
    private int torque;
    private String fuelType;
    private String cylinderConfiguration;
    private String engineType;
}
