package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.StatusDTO;
import pl.myproject.car_rental_api.dto.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.entity.CarAvailability;
import pl.myproject.car_rental_api.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface CarAvailabilityService {

    List<StatusDTO> getCarAvailabilityList(long carId);

    CarAvailability isCarAvailable(LocalDate startDate, LocalDate endDate, int carId);

    void changeCarAvailability(CarAvailability carAvailability, LocalDate reservationStartDate, LocalDate reservationEndDate, Car car);

    void isCarAvailableForNewPeriod(Reservation reservation, UpdateReservationDateDTO reservationDateDTO);
}
