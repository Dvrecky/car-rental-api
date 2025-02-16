package pl.myproject.car_rental_api.dto.gearbox;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GearboxDTO {

    private int id;
    private String name;
    private String producer;
    private int numberOfGears;
    private String type;
}
