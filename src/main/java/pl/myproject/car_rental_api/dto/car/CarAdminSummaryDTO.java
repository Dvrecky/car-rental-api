package pl.myproject.car_rental_api.dto.car;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarAdminSummaryDTO {

    private int id;
    private String registrationNumber;
    private String vin;
}
