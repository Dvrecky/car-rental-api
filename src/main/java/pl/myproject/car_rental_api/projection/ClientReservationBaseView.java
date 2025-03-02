package pl.myproject.car_rental_api.projection;

import java.time.LocalDateTime;

public interface ClientReservationBaseView {

    long getId();
    LocalDateTime getBookingDate();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    String getStatus();
    int getCarId();
    String getCarFullName();
    String getPhotoUrl();
}
