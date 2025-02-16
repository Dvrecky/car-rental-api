package pl.myproject.car_rental_api.dto.other;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CarConditionDTO {

    private int id;
    private String name;
    private short isRentable;
    private String description;
}
