package pl.myproject.car_rental_api.dto.car;

import lombok.*;
import pl.myproject.car_rental_api.dto.other.CarConditionDTO;
import pl.myproject.car_rental_api.dto.model.ModelDTO;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CarDetailsDTO {

    private int id;
    private String registrationNumber;
    private String vin;
    private LocalDate lastServiceDate;
    private int mileage;
    private LocalDate insuranceExpiryDate;
    private int rentalPricePerDay;
    private int basePrice;
    private ModelDTO model;
    private CarConditionDTO carCondition;
}
