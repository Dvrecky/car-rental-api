package pl.myproject.car_rental_api.projection;

import java.math.BigDecimal;

public interface CarBaseInfoProjection {

    int getId();
    int getRentalPricePerDay();
    String getFullName();   // brand + name
    String getTypeOfDrive();
    int getNumberOfSeats();
    BigDecimal getAccelerationTime();
    BigDecimal getCapacity();
    int getHorsepower();
    int getTorque();
    String getCylinderConfiguration();
    int getNumberOfGears();
}
