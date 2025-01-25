package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.StatusDTO;
import pl.myproject.car_rental_api.dto.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.entity.CarAvailability;
import pl.myproject.car_rental_api.entity.Reservation;

import java.util.List;

public interface CarAvailabilityService {

    List<StatusDTO> getCarAvailabilityList(long carId);

    CarAvailability isCarAvailable(Reservation reservation);

    void changeCarAvailability(CarAvailability carAvailability, Reservation reservation);

    void isCarAvailableForNewPeriod(Reservation reservation, UpdateReservationDateDTO reservationDateDTO);
}
