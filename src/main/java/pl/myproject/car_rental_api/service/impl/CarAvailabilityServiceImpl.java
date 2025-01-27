package pl.myproject.car_rental_api.service.impl;

import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.StatusDTO;
import pl.myproject.car_rental_api.dto.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.entity.CarAvailability;
import pl.myproject.car_rental_api.repository.CarAvailabilityRepository;
import pl.myproject.car_rental_api.service.CarAvailabilityService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
    public CarAvailability getCarAvailability(long carId, LocalDate startDate, LocalDate endDate) {
        return carAvailabilityRepository.getCarAvailability(carId, startDate, endDate)
                    .orElseThrow(() -> new NoSuchElementException("Car availability not found"));
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
    public CarAvailability isCarAvailableForNewPeriod(long carId, LocalDate startDate, LocalDate endDate, UpdateReservationDateDTO reservationDateDTO) {

        // new period
        LocalDate newStartDate = reservationDateDTO.getNewStartDate().toLocalDate();
        LocalDate newEndDate = reservationDateDTO.getNewEndDate().toLocalDate();

        // checking if car is available for various cases
        if(startDate.isEqual(reservationDateDTO.getNewStartDate().toLocalDate()) && endDate.isBefore(newEndDate)) {

            // checking if car is available
            return carAvailabilityRepository.checkIfNewDateIsAvailable(carId, newStartDate, newEndDate)
                    .orElseThrow(() -> new NoSuchElementException("New reservation period is not available"));

        } else if (startDate.isEqual(reservationDateDTO.getNewStartDate().toLocalDate()) && endDate.isAfter(newEndDate)) {
            return carAvailabilityRepository.getCarAvailability(carId, startDate, endDate)
                    .orElseThrow(() -> new NoSuchElementException("New reservation period is not available"));
        }

        return null;
    }

    @Override
    public void changeCarAvailabilityForNewPeriod(long carId, CarAvailability carAvailability, LocalDate startDate, LocalDate endDate, UpdateReservationDateDTO reservationDateDTO) {

        // new period
        LocalDate newStartDate = reservationDateDTO.getNewStartDate().toLocalDate();
        LocalDate newEndDate = reservationDateDTO.getNewEndDate().toLocalDate();

        // updating car availability according to given case
        if(startDate.isEqual(reservationDateDTO.getNewStartDate().toLocalDate()) && endDate.isBefore(newEndDate)) {

            CarAvailability currentCarAvailability = carAvailabilityRepository.getCarAvailability(carId, startDate, endDate)
                            .orElseThrow( () -> new NoSuchElementException("Car availability not found"));

            carAvailabilityRepository.updateEndDate(newEndDate, currentCarAvailability.getId());

            carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailability.getId());

        } else if (startDate.isEqual(reservationDateDTO.getNewStartDate().toLocalDate()) && endDate.isAfter(newEndDate)) {
            // if new reservation period is between old reservation period

            // updating end date for availability
            carAvailabilityRepository.updateEndDate(newEndDate, carAvailability.getId());

            Optional<CarAvailability> carAvailability1 = carAvailabilityRepository.checkStatus(carId, endDate.plusDays(1), "AVAILABLE");

            if(carAvailability1.isPresent()) {

                carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailability1.get().getId());

            } else {
                carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), endDate, carAvailability.getCar()));
            }
        }
    }
}
