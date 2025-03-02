package pl.myproject.car_rental_api.projection;

import java.time.LocalDateTime;

public interface ClientRentBaseView {

    long getId();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    String getStatus();
    String getCarFullName();
    String getPhotoUrl();
    long getReservationId();
    long getCarId();
}
