package pl.myproject.car_rental_api.dto.car;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import pl.myproject.car_rental_api.dto.model.AddModelDTO;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddCarDTO {

    @NotBlank(message = "registration number cannot be blank")
    @Size(min = 7, max = 7, message = "registration number must have length of 7")
    private String registrationNumber;

    @NotBlank(message = "vin cannot be blank")
    @Size(min = 17, max = 17, message = "vin must have length of 17")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "vin contains illegal characters")
    private String vin;

    @NotNull(message = "last service date can't be empty")
    @PastOrPresent(message = "last service date can't be from the future")
    private LocalDate lastServiceDate;

    @NotNull(message = "mileage can't be null")
    @Min(0)
    private int mileage;

    @NotNull(message = "insurance expiry date can't be empty")
    @Future(message = "insurance expiry date must be in future")
    private int insuranceExpiryDate;

    @Valid
    private AddModelDTO modelDTO;
}
