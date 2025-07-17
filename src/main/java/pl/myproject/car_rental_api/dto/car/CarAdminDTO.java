package pl.myproject.car_rental_api.dto.car;

import lombok.Data;
import pl.myproject.car_rental_api.dto.model.ModelAdminDTO;

import java.time.LocalDate;

@Data
public class CarAdminDTO {

    private int id;
    private String registrationNumber;
    private String vin;
    private LocalDate lastServiceDate;
    private int mileage;
    private LocalDate insuranceExpiryDate;
    private int rentalPricePerDay;
    private int basePrice;
    private ModelAdminDTO modelAdminDTO;
}
