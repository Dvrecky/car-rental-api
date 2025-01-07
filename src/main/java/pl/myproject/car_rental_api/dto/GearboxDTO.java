package pl.myproject.car_rental_api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
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
