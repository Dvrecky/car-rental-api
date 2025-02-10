package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.StatusDTO;
import pl.myproject.car_rental_api.dto.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.entity.CarAvailability;

import java.time.LocalDate;
import java.util.List;

public interface CarAvailabilityService {

    List<StatusDTO> getCarAvailabilityList(long carId);

    CarAvailability getCarAvailability(long carId, LocalDate startDate, LocalDate endDate);

    CarAvailability isCarAvailable(LocalDate startDate, LocalDate endDate, int carId);

    void changeCarAvailability(CarAvailability carAvailability, LocalDate reservationStartDate, LocalDate reservationEndDate, Car car);

    CarAvailability isCarAvailableForNewPeriod(long carId, LocalDate startDate, LocalDate endDate, UpdateReservationDateDTO reservationDateDTO);

    void changeCarAvailabilityForNewPeriod(long carId, CarAvailability carAvailability, LocalDate startDate, LocalDate endDate, UpdateReservationDateDTO reservationDateDTO);

    void makeCarAvailable(int carId, LocalDate startDate, LocalDate endDate);
}
