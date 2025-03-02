package pl.myproject.car_rental_api.service.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.reservation.AddReservationDTO;
import pl.myproject.car_rental_api.dto.reservation.ReservationDTO;
import pl.myproject.car_rental_api.dto.reservation.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.entity.CarAvailability;
import pl.myproject.car_rental_api.entity.Reservation;
import pl.myproject.car_rental_api.projection.ClientReservationBaseView;
import pl.myproject.car_rental_api.repository.ReservationRepository;
import pl.myproject.car_rental_api.service.CarAvailabilityService;
import pl.myproject.car_rental_api.service.CarService;
import pl.myproject.car_rental_api.service.ReservationService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        Car car = carService.getCarWithDetailsById(carId);
        reservation.setCar(car);

        // changing availability for reservation period
        carAvailabilityService.changeCarAvailability(carAvailability, startDate, endDate, car);

        // saving new reservation to db
        Reservation newReservation = reservationRepository.save(reservation);

        // returning reservation with car details
        return toReservationDTOModelMapper.map(newReservation, ReservationDTO.class);
    }

    @Transactional
    @Override
    public ReservationDTO updateReservationPeriod(UpdateReservationDateDTO reservationDateDTO, long reservationId) {

        // getting reservation with car
        Reservation reservation = reservationRepository.findReservationWithCarDetailsById(reservationId)
                        .orElseThrow( () -> new NoSuchElementException("Reservation with id: " + reservationId + " not found"));

        // checking if car is available for new period
        long carId = reservation.getCar().getId();
        LocalDate startDate = reservation.getStartDate().toLocalDate();
        LocalDate endDate = reservation.getEndDate().toLocalDate();

        CarAvailability carAvailability = carAvailabilityService.isCarAvailableForNewPeriod(carId, startDate, endDate, reservationDateDTO);

        // changing car availability
        carAvailabilityService.changeCarAvailabilityForNewPeriod(carId, carAvailability, startDate, endDate, reservationDateDTO);

        reservation.setStartDate(reservationDateDTO.getNewStartDate());
        reservation.setEndDate(reservationDateDTO.getNewEndDate());

        // updating reservation with new period in db
        reservationRepository.updateReservationPeriod(reservationId, reservationDateDTO.getNewStartDate(), reservationDateDTO.getNewEndDate());

        return toReservationDTOModelMapper.map(reservation, ReservationDTO.class);
    }

    @Override
    public ReservationDTO cancelReservation(long reservationId) {

        Optional<Reservation> optionalReservation = reservationRepository.findReservationWithCarDetailsById(reservationId);

        if(optionalReservation.isEmpty()) {
            throw new NoSuchElementException("Reservation with ID: " + reservationId + " not found");
        }

        Reservation reservationToCancel = optionalReservation.get();
        reservationToCancel.setStatus("CANCELED");
        reservationRepository.changeStatus(reservationId, "CANCELED");

        carAvailabilityService.makeCarAvailable(reservationToCancel.getCar().getId(), reservationToCancel.getStartDate().toLocalDate(), reservationToCancel.getEndDate().toLocalDate());

        return toReservationDTOModelMapper.map(reservationToCancel, ReservationDTO.class);
    }

    @Override
    public void isReservationConfirmed(long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow( () -> new NoSuchElementException("Reservation not found for ID: " + reservationId));

        if(!reservation.getStatus().equalsIgnoreCase("CONFIRMED")) {
            throw new NoSuchElementException("Reservation has a " + reservation.getStatus() + " status");
        }
    }

    @Override
    public List<ClientReservationBaseView> getReservationsByClientId(long clientId) {

        return reservationRepository.findAllReservationsBaseView(clientId);
    }
}
