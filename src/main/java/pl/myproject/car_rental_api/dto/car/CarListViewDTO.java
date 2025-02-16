package pl.myproject.car_rental_api.dto.car;

import lombok.Getter;

@Getter
public class CarListViewDTO {

    private int id;
    private String registrationNumber;
    private String vin;
    private String fullName;
}
