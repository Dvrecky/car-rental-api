package pl.myproject.car_rental_api.dto.gearbox;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddGearboxDTO {

    @NotBlank(message = "gearbox name can't be blank")
    @Size(min = 3, max = 30, message = "gearbox name must be at most 30 characters, and must have at least 3 characters")
    private String name;

    @NotBlank(message = "producer name can't be blank")
    @Size(min = 3, max = 20, message = "producer name must be at most 20 characters, and must have at least 3 characters")
    private String producer;

    @NotNull(message = "number of gears can't be empty")
    @Min(5)
    @Max(10)
    private int numberOfGears;

    @NotBlank(message = "gearbox type can't be blank")
    @Size(min = 3, max = 20, message = "gearbox name must be at most 20 characters, and must have at least 3 characters")
    private String type;
}
