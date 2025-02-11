package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.AddReservationDTO;
import pl.myproject.car_rental_api.dto.ReservationDTO;
import pl.myproject.car_rental_api.dto.UpdateReservationDateDTO;

public interface ReservationService {

    ReservationDTO addReservation(AddReservationDTO addReservationDTO);

    ReservationDTO updateReservationPeriod(UpdateReservationDateDTO reservationDateDTO, long reservationId);

    ReservationDTO cancelReservation(long reservationId);

    void isReservationConfirmed(long reservationId);
}
