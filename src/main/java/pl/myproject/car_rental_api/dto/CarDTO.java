package pl.myproject.car_rental_api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CarDTO {

    private int id;
    private String registrationNumber;
    private LocalDate lastServiceDate;
    private int mileage;
    private LocalDate insuranceExpiryDate;
    private double rentalPricePerDay;
    private double basePrice;
    private ModelDTO model;
}
