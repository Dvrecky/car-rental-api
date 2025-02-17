package pl.myproject.car_rental_api.dto.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import pl.myproject.car_rental_api.dto.engine.AddEngineDTO;
import pl.myproject.car_rental_api.dto.gearbox.AddGearboxDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddModelDTO {

    @NotBlank(message = "name can't be blank")
    @Size(min = 3, max = 40, message = "name must be at most 40 characters, and must have at least 3 character")
    private String name;

    @NotBlank(message = "type can't be blank")
    @Size(min = 3, max = 20, message = "type must be at most 20 characters, and must have at least 3 character")
    private String type;

    @NotNull(message = "production year can't be null")
    @Past(message = "production year must be in past")
    private LocalDate productionYear;

    @NotBlank(message = "brand can't be blank")
    @Size(min = 3, max = 20, message = "brand must be at most 20 characters, and must have at least 3 character")
    private String brand;

    @NotBlank(message = "brand country can't be blank")
    @Size(min = 3, max = 20, message = "brand country must be at most 20 characters, and must have at least 3 character")
    private String brandCountry;

    @NotBlank(message = "color can't be blank")
    @Size(min = 3, max = 30, message = "color must be at most 30 characters, and must have at least 3 character")
    private String color;

    @NotBlank(message = "type of drive can't be blank")
    @Size(min = 3, max = 3, message = "type of drive must have length of 3")
    private String typeOfDrive;

    @NotNull(message = "number of doors can't be empty")
    @Min(2)
    @Max(7)
    private int numberOfDoors;

    @NotBlank(message = "body type can't be blank")
    @Size(min = 3, max = 20, message = "body type must have at most 20 characters and at least 3 characters")
    private String bodyType;

    @NotNull(message = "number of seats can't be empty")
    @Min(2)
    @Max(7)
    private int numberOfSeats;

    @NotBlank(message = "environmental label can't be blank")
    @Size(min = 3, max = 20, message = "environmental label must have at most 20 characters and at least 3 characters")
    private String environmentalLabel;

    @NotNull(message = "fuel consumption can't be empty")
    @DecimalMin(value = "00.00", message = "fuel consumption must be over 0")
    @DecimalMax(value = "100.00", message = "fuel consumption must be at most 100")
    private BigDecimal fuelConsumption;

    @NotNull(message = "co2 emission can't be empty")
    @DecimalMin(value = "00.00", message = "co2 emission must be over 0")
    @DecimalMax(value = "100.00", message = "co2 emission must be at most 100")
    private BigDecimal CO2Emissions;

    @NotNull(message = "weight can't be empty")
    @Min(0)
    private int weight;

    @NotNull(message = "acceleration time can't be empty")
    @DecimalMin(value = "00.00", message = "acceleration time can't be lower than 00.00")
    private BigDecimal accelerationTime;

    @NotBlank(message = "photo url can't be blank")
    @Max(value = 255, message = "photo url must have at most 255 characters")
    private String photoUrl;

    @Min(0)
    private int averagePrice;

    @NotBlank(message = "description can't be blank")
    @Size(max = 255, message = "description must have at most 255 characters")
    private String description;

    @Valid
    private AddEngineDTO engineDTO;

    @Valid
    private AddGearboxDTO gearboxDTO;
}
