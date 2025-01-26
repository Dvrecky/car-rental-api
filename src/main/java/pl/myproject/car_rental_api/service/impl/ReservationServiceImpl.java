package pl.myproject.car_rental_api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.AddReservationDTO;
import pl.myproject.car_rental_api.dto.ReservationDTO;
import pl.myproject.car_rental_api.dto.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.entity.CarAvailability;
import pl.myproject.car_rental_api.entity.Reservation;
import pl.myproject.car_rental_api.repository.CarRepository;
import pl.myproject.car_rental_api.repository.ReservationRepository;
import pl.myproject.car_rental_api.service.CarAvailabilityService;
import pl.myproject.car_rental_api.service.CarService;
import pl.myproject.car_rental_api.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final CarService carService;
    private final CarAvailabilityService carAvailabilityService;
    private final ReservationRepository reservationRepository;
    private final ModelMapper defaultModelMapper;
    private final ModelMapper toReservationDTOModelMapper;

    public ReservationServiceImpl(CarAvailabilityService carAvailabilityService, ReservationRepository reservationRepository, @Qualifier("defaultModelMapper") ModelMapper defaultModelMapper, CarService carService, @Qualifier("toReservationDTOModelMapper") ModelMapper toReservationDTOModelMapper) {
        this.carAvailabilityService = carAvailabilityService;
        this.reservationRepository = reservationRepository;
        this.defaultModelMapper = defaultModelMapper;
        this.carService = carService;
        this.toReservationDTOModelMapper = toReservationDTOModelMapper;
    }

    @Override
    public ReservationDTO addReservation(AddReservationDTO addReservationDTO) {

        // checking if car is available for given period
        LocalDate startDate = addReservationDTO.getStartDate().toLocalDate();
        LocalDate endDate = addReservationDTO.getEndDate().toLocalDate();
        int carId = addReservationDTO.getCarId();

        // checking if given car is available for given period
        CarAvailability carAvailability = carAvailabilityService.isCarAvailable(startDate, endDate, carId);

        // mapping
        Reservation reservation = defaultModelMapper.map(addReservationDTO, Reservation.class);

        // getting car by id for given reservation
        Car car = carService.getCarByIdWithDetails(carId);
        reservation.setCar(car);

        // changing availability for reservation period
        carAvailabilityService.changeCarAvailability(carAvailability, startDate, endDate, car);

        // saving new reservation to db
        Reservation newReservation = reservationRepository.save(reservation);

        // returning reservation with car details
        return toReservationDTOModelMapper.map(newReservation, ReservationDTO.class);
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

        return defaultModelMapper.map(updatedReservation, ReservationDTO.class);
    }
}
