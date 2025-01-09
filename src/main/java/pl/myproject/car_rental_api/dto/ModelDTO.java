package pl.myproject.car_rental_api.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ModelDTO {

    private int id;
    private String name;
    private String type;
    private LocalDate productionYear;
    private String brand;
    private String brandCountry;
    private String color;
    private String typeOfDrive;
    private int numberOfDoors;
    private String bodyType;
    private int numberOfSeats;
    private String environmentalLabel;
    private String fuelConsumption;
    private String CO2Emissions;
    private int weight;
    private double accelerationTime;
    private String photoUrl;
    private int averagePrice;
    private String description;
    private EngineDTO engine;
    private GearboxDTO gearbox;
}
