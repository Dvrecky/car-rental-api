package pl.myproject.car_rental_api.service.impl;

import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.StatusDTO;
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
    public CarAvailability isCarAvailable(Reservation reservation) {

        LocalDate startDate = reservation.getStartDate().toLocalDate();
        LocalDate endDate = reservation.getEndDate().toLocalDate();

        int carId = reservation.getCar().getId();

        CarAvailability carAvailability = carAvailabilityRepository
                .isCarAvailable(carId, startDate, endDate)
                .orElseThrow( () -> new NoSuchElementException("Car with id: " + carId + " is not available between " +
                        startDate + " and " + endDate));

        return carAvailability;
    }

    @Override
    public void changeCarAvailability(CarAvailability carAvailability, Reservation reservation) {

        long availabilityId = carAvailability.getId();

        LocalDate reservationStartDate = reservation.getStartDate().toLocalDate();
        LocalDate reservationEndDate = reservation.getEndDate().toLocalDate();

        if(reservationStartDate.isEqual(carAvailability.getStartDate()) && reservationEndDate.isEqual(carAvailability.getEndDate())) {

            carAvailabilityRepository.updateStatus(availabilityId, "RESERVED");

        } else if(reservationStartDate.isEqual(carAvailability.getStartDate())) {

            carAvailabilityRepository.updateEndDateAndStatus(reservationEndDate, availabilityId);

            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", reservationEndDate.plusDays(1), carAvailability.getEndDate(), reservation.getCar()));

        } else if (reservationEndDate.isEqual(carAvailability.getEndDate())) {

            carAvailabilityRepository.updateStartDateAndStatus(reservationStartDate, availabilityId);

            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), reservationStartDate.minusDays(1), reservation.getCar()));

        } else {
            LocalDate newEndDate = reservationStartDate.minusDays(1);
            carAvailabilityRepository.updateEndDate(newEndDate, availabilityId);

            carAvailabilityRepository.save(new CarAvailability("RESERVED", reservationStartDate, reservationEndDate, reservation.getCar()));

            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", reservationEndDate.plusDays(1), carAvailability.getEndDate(), reservation.getCar()));
        }
    }
}
