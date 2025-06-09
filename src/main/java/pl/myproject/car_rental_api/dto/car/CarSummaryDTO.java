package pl.myproject.car_rental_api.dto.car;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CarSummaryDTO {

    private int id;
    private int rentalPricePerDay;
    private String name;
    private int numberOfSeats;
    private BigDecimal accelerationTime;
    private String photoUrl;
    private int horsepower;
    private int torque;
    private String gearboxType;
}
