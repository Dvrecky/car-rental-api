package pl.myproject.car_rental_api.service.impl;

import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.entity.CarAvailability;
import pl.myproject.car_rental_api.entity.Reservation;
import pl.myproject.car_rental_api.repository.ReservationRepository;
import pl.myproject.car_rental_api.service.CarAvailabilityService;
import pl.myproject.car_rental_api.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final CarAvailabilityService carAvailabilityService;
    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(CarAvailabilityService carAvailabilityService, ReservationRepository reservationRepository) {
        this.carAvailabilityService = carAvailabilityService;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation addReservation(Reservation reservation) {

        CarAvailability carAvailability = carAvailabilityService.isCarAvailable(reservation);

        carAvailabilityService.changeCarAvailability(carAvailability, reservation);

        Reservation newReservation = reservationRepository.save(reservation);

        return newReservation;
    }
}
