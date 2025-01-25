package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.ReservationDTO;
import pl.myproject.car_rental_api.dto.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.entity.Reservation;

public interface ReservationService {

    Reservation addReservation(Reservation reservation);

    ReservationDTO updateReservationPeriod(UpdateReservationDateDTO reservationDateDTO, long reservationId);
}
