package pl.myproject.car_rental_api.dto.engine;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddEngineDTO {

    @NotNull(message = "capacity can't me empty")
    @DecimalMin(value = "1.00", message = "capacity can't be lower than 1.00")
    @DecimalMax(value = "20.00", message = "capacity can't exceed 20.00")
    private BigDecimal capacity;

    @NotNull(message = "capacity can't be empty")
    @Min(0)
    private int horsepower;

    @NotNull(message = "torque can't be empty")
    @Min(0)
    private int torque;

    @NotBlank(message = "fuel type can't be blank")
    @Size(min = 1, max = 20, message = "fuel type must be at most 20 characters, and must have at least 1 character")
    private String fuelType;

    @NotBlank(message = "cylinder configuration can't be blank")
    @Size(min = 1, max = 10, message = "cylinder configuration must be at most 10 characters, and must have at least 1 character")
    private String cylinderConfiguration;

    @NotBlank(message = "engine type can't be blank")
    @Size(min = 1, max = 10, message = "engine type must be at most 10 characters, and must have at least 1 character")
    private String engineType;
}
