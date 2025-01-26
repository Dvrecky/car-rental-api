package pl.myproject.car_rental_api.service.impl;

import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.StatusDTO;
import pl.myproject.car_rental_api.dto.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.entity.CarAvailability;
import pl.myproject.car_rental_api.entity.Reservation;
import pl.myproject.car_rental_api.repository.CarAvailabilityRepository;
import pl.myproject.car_rental_api.service.CarAvailabilityService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CarAvailabilityServiceImpl implements CarAvailabilityService {

    private final CarAvailabilityRepository carAvailabilityRepository;

    public CarAvailabilityServiceImpl(CarAvailabilityRepository carAvailabilityRepository) {
        this.carAvailabilityRepository = carAvailabilityRepository;
    }

    public List<StatusDTO> getCarAvailabilityList(long carId) {
        List<CarAvailability> carAvailabilities = carAvailabilityRepository.findAllByCarId(carId);
        List<StatusDTO> carAvailabilityList = carAvailabilities.stream()
                .map(carAv -> new StatusDTO(carAv.getStartDate(), carAv.getEndDate(), carAv.getStatus()))
                .toList();

        return carAvailabilityList.stream()
                .sorted(Comparator.comparing(StatusDTO::getFrom))
                .collect(Collectors.toList());
    }

    @Override
    public CarAvailability isCarAvailable(LocalDate startDate, LocalDate endDate, int carId) {

        CarAvailability carAvailability = carAvailabilityRepository
                .isCarAvailable(carId, startDate, endDate)
                .orElseThrow( () -> new NoSuchElementException("Car with id: " + carId + " is not available between " +
                        startDate + " and " + endDate));

        return carAvailability;
    }

    @Override
    public void changeCarAvailability(CarAvailability carAvailability, LocalDate reservationStartDate, LocalDate reservationEndDate, Car car) {

        long availabilityId = carAvailability.getId();

        if(reservationStartDate.isEqual(carAvailability.getStartDate()) && reservationEndDate.isEqual(carAvailability.getEndDate())) {

            carAvailabilityRepository.updateStatus(availabilityId, "RESERVED");

        } else if(reservationStartDate.isEqual(carAvailability.getStartDate())) {

            carAvailabilityRepository.updateEndDateAndStatus(reservationEndDate, availabilityId);

            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", reservationEndDate.plusDays(1), carAvailability.getEndDate(), car));

        } else if (reservationEndDate.isEqual(carAvailability.getEndDate())) {

            carAvailabilityRepository.updateStartDateAndStatus(reservationStartDate, availabilityId);

            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), reservationStartDate.minusDays(1), car));

        } else {
            LocalDate newEndDate = reservationStartDate.minusDays(1);
            carAvailabilityRepository.updateEndDate(newEndDate, availabilityId);

            carAvailabilityRepository.save(new CarAvailability("RESERVED", reservationStartDate, reservationEndDate, car));

            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", reservationEndDate.plusDays(1), carAvailability.getEndDate(), car));
        }
    }

    @Override
    public void isCarAvailableForNewPeriod(Reservation reservation, UpdateReservationDateDTO reservationDateDTO) {

        long carId = reservation.getCar().getId();

        // current period
        LocalDate startDate = reservation.getStartDate().toLocalDate();
        LocalDate endDate = reservation.getEndDate().toLocalDate();

        // new period
        LocalDate newStartDate = reservationDateDTO.getNewStartDate().toLocalDate();
        LocalDate newEndDate = reservationDateDTO.getNewEndDate().toLocalDate();

        if(startDate.isEqual(reservationDateDTO.getNewStartDate().toLocalDate()) && endDate.isBefore(newEndDate)) {
            CarAvailability carAvailabilities = carAvailabilityRepository.checkIfNewDateIsAvailable(carId, newStartDate, newEndDate)
                    .orElseThrow(() -> new NoSuchElementException("New reservation period is not available"));

            CarAvailability carAvailability = carAvailabilityRepository.getCarAvailability(carId, startDate, endDate)
                    .orElseThrow(() -> new NoSuchElementException("Car availability not found"));

            carAvailabilityRepository.updateEndDate(newEndDate, carAvailability.getId());

            carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailabilities.getId());
        }
    }
}
