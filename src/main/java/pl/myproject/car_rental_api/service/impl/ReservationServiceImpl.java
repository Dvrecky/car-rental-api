package pl.myproject.car_rental_api.service.impl;

import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.ReservationDTO;
import pl.myproject.car_rental_api.dto.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.entity.CarAvailability;
import pl.myproject.car_rental_api.entity.Reservation;
import pl.myproject.car_rental_api.repository.ReservationRepository;
import pl.myproject.car_rental_api.service.CarAvailabilityService;
import pl.myproject.car_rental_api.service.ReservationService;


import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final CarAvailabilityService carAvailabilityService;
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;

    public ReservationServiceImpl(CarAvailabilityService carAvailabilityService, ReservationRepository reservationRepository, ModelMapper modelMapper, EntityManager entityManager) {
        this.carAvailabilityService = carAvailabilityService;
        this.reservationRepository = reservationRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Reservation addReservation(Reservation reservation) {

        CarAvailability carAvailability = carAvailabilityService.isCarAvailable(reservation);

        carAvailabilityService.changeCarAvailability(carAvailability, reservation);

        Reservation newReservation = reservationRepository.save(reservation);

        return newReservation;
    }

    @Override
    public ReservationDTO updateReservationPeriod(UpdateReservationDateDTO reservationDateDTO, long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow( () -> new NoSuchElementException("Reservation with id : " + reservationId + " not found"));

        carAvailabilityService.isCarAvailableForNewPeriod(reservation, reservationDateDTO);

        LocalDateTime newStartDate = reservationDateDTO.getNewStartDate();
        LocalDateTime newEndDate = reservationDateDTO.getNewEndDate();

        reservationRepository.updateReservationPeriod(reservationId, newStartDate, newEndDate);

        Reservation updatedReservation = reservationRepository.findById(reservationId)
                .orElseThrow( () -> new NoSuchElementException("Reservation with id : " + reservationId + " not found"));

        return modelMapper.map(updatedReservation, ReservationDTO.class);
    }
}
