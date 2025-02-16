package pl.myproject.car_rental_api.service.impl;

import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.other.StatusDTO;
import pl.myproject.car_rental_api.dto.reservation.UpdateReservationDateDTO;
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

            carAvailabilityRepository.updateEndDateAndStatus(reservationEndDate, availabilityId, "RESERVED");

            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", reservationEndDate.plusDays(1), carAvailability.getEndDate(), car));

        } else if (reservationEndDate.isEqual(carAvailability.getEndDate())) {

            carAvailabilityRepository.updateStartDateAndStatus(reservationStartDate, availabilityId, "RESERVED");

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

        // if new reservation period scope is within current period
        if (((startDate.isEqual(newStartDate) || startDate.isBefore(newStartDate)) &&
                (endDate.isEqual(newEndDate) || endDate.isAfter(newEndDate))) ||
                (newStartDate.isBefore(startDate) && newEndDate.isAfter(endDate))) {

            // then return car availability for current reservation period
            return carAvailabilityRepository.getCarAvailability(carId, startDate, endDate)
                    .orElseThrow(() -> new NoSuchElementException("New reservation period is not available"));

        } else {
            // checking if car is available
            return carAvailabilityRepository.checkIfNewDateIsAvailable(carId, newStartDate, newEndDate)
                    .orElseThrow(() -> new NoSuchElementException("New reservation period is not available"));
        }
    }

    @Override
    public void changeCarAvailabilityForNewPeriod(long carId, CarAvailability carAvailability, LocalDate startDate, LocalDate endDate, UpdateReservationDateDTO reservationDateDTO) {

        // new period
        LocalDate newStartDate = reservationDateDTO.getNewStartDate().toLocalDate();
        LocalDate newEndDate = reservationDateDTO.getNewEndDate().toLocalDate();

        // updating car availability according to given case
        if ((startDate.isEqual(newStartDate) || startDate.isBefore(newStartDate)) &&
                (endDate.isEqual(newEndDate) || endDate.isAfter(newEndDate))) {

            if(startDate.isEqual(newStartDate)) {   // if new reservation start date is the same
                // if new reservation period is between old reservation period

                // updating end date for car availability
                carAvailabilityRepository.updateEndDate(newEndDate, carAvailability.getId());

                Optional<CarAvailability> carAvailability1 = carAvailabilityRepository.checkStatusByStartDate(carId, endDate.plusDays(1), "AVAILABLE");

                if(carAvailability1.isPresent()) {

                    carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailability1.get().getId());

                } else {

                    carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), endDate, carAvailability.getCar()));
                }

            } else if(endDate.isEqual(newEndDate)){ // if new reservation end date is the same

                // updating start date for car availability
                carAvailabilityRepository.updateStartDate(newStartDate, carAvailability.getId());

                Optional<CarAvailability> carAvailabilityOptional = carAvailabilityRepository.checkStatusByEndDate(carId, startDate.minusDays(1), "AVAILABLE");

                if(carAvailabilityOptional.isPresent()) {
                    carAvailabilityRepository.updateEndDate(newStartDate.minusDays(1), carAvailabilityOptional.get().getId());
                } else {
                    carAvailabilityRepository.save(new CarAvailability("AVAILABLE", startDate, newStartDate.minusDays(1), carAvailability.getCar()));
                }

            } else {    // if new reservation period is between the current one

                // changing period for existing car availability (cutting reservation period)
                carAvailabilityRepository.changePeriod(carAvailability.getId(), newStartDate, newEndDate);

                Optional<CarAvailability> carAvailabilityOptional = carAvailabilityRepository.checkStatusByEndDate(carId, startDate.minusDays(1), "AVAILABLE");

                if(carAvailabilityOptional.isPresent()) {
                    carAvailabilityRepository.updateEndDate(newStartDate.minusDays(1), carAvailabilityOptional.get().getId());
                } else {
                    carAvailabilityRepository.save(new CarAvailability("AVAILABLE", startDate, newStartDate.minusDays(1), carAvailability.getCar()));
                }

                Optional<CarAvailability> carAvailabilityOptional1 = carAvailabilityRepository.checkStatusByStartDate(carId, endDate.plusDays(1) , "AVAILABLE");

                if(carAvailabilityOptional1.isPresent()) {
                    carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailabilityOptional1.get().getId());
                } else {
                    carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), endDate, carAvailability.getCar()));
                }
            }

        } else if (newStartDate.isBefore(startDate) && newEndDate.isAfter(endDate)) {

            List<CarAvailability> carAvailabilities = carAvailabilityRepository.checkIfNewPeriodIsAvailable(carId, newStartDate, newEndDate)
                    .orElseThrow( () -> new NoSuchElementException("New period is not available"));

            if (carAvailabilities.size() != 2) {
                throw new NoSuchElementException("Car with ID: " + carId + " is not available for given period");
            }

            carAvailabilityRepository.changePeriod(carAvailability.getId(), newStartDate, newEndDate);

            carAvailabilities.sort(Comparator.comparing(CarAvailability::getStartDate));
            CarAvailability carAvailabilityBefore = carAvailabilities.getFirst();
            CarAvailability carAvailabilityAfter = carAvailabilities.getLast();

            if (carAvailabilityBefore.getStartDate().isEqual(newStartDate)) {
                carAvailabilityRepository.deleteById(carAvailabilityBefore.getId());
            } else {
                carAvailabilityRepository.updateEndDate(newStartDate.minusDays(1), carAvailabilityBefore.getId());
            }

            if (carAvailabilityAfter.getEndDate().isEqual(newEndDate)) {
                carAvailabilityRepository.deleteById(carAvailabilityAfter.getId());
            } else {
                carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailabilityAfter.getId());
            }

        }
        // -----------------------------------------------------------------------------------------------
        // case if new reservation period is not included in old period
        else if(newStartDate.isAfter(endDate) || newEndDate.isBefore(startDate)) {

            CarAvailability currentCarAvailability = carAvailabilityRepository.getCarAvailability(carId, startDate, endDate)
                    .orElseThrow( () -> new NoSuchElementException("Car availability not found"));

            Optional<CarAvailability> optionalCarAvailabilityBefore = carAvailabilityRepository.checkStatusByEndDate(carId, startDate.minusDays(1), "AVAILABLE");
            Optional<CarAvailability> optionalCarAvailabilityAfter = carAvailabilityRepository.checkStatusByStartDate(carId, endDate.plusDays(1), "AVAILABLE");

            // case when new period is after old one
            if (newEndDate.isAfter(endDate)) {

                // if car is available before and after current reservation period
                if (optionalCarAvailabilityBefore.isPresent() && optionalCarAvailabilityAfter.isPresent()) {

                    CarAvailability carAvBefore = optionalCarAvailabilityBefore.get();
                    CarAvailability carAvAfter = optionalCarAvailabilityAfter.get();

                    // if new reservation period is included in car availability right after current reservation period
                    if(carAvailability.getId() == carAvAfter.getId()) {
                        carAvailabilityRepository.updateEndDate(newStartDate.minusDays(1), carAvBefore.getId());
                        carAvailabilityRepository.deleteById(currentCarAvailability.getId());
                        carAvailabilityRepository.changePeriodAndStatus(carAvAfter.getId(), newStartDate, newEndDate, "RESERVED");

                        if(!newEndDate.isEqual(carAvAfter.getEndDate())) {

                            Optional<CarAvailability> carAvailabilityOptional = carAvailabilityRepository.checkStatusByStartDate(carId, carAvAfter.getEndDate().plusDays(1), "AVAILABLE");
                            if(carAvailabilityOptional.isPresent()) {
                                carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailabilityOptional.get().getId());
                            } else {
                                carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvAfter.getEndDate(), carAvailability.getCar()));
                            }
                        }
                    } else {

                        carAvailabilityRepository.updateEndDate(carAvAfter.getEndDate(), carAvBefore.getId());
                        carAvailabilityRepository.deleteById(currentCarAvailability.getId());
                        carAvailabilityRepository.deleteById(carAvAfter.getId());

                        if(newStartDate.isEqual(carAvailability.getStartDate()) && newEndDate.isEqual(carAvailability.getEndDate())) {

                            carAvailabilityRepository.updateStatus(carAvailability.getId(), "RESERVED");
                        } else if(newStartDate.isEqual(carAvailability.getStartDate())) {
                            carAvailabilityRepository.updateEndDateAndStatus(newEndDate, carAvailability.getId(), "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                        } else if(newEndDate.isEqual(carAvailability.getEndDate())) {
                            carAvailabilityRepository.updateStartDateAndStatus(newStartDate, carAvailability.getId(), "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                        } else {

                            carAvailabilityRepository.changePeriodAndStatus(carAvailability.getId(), newStartDate, newEndDate, "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                        }
                    }
                } else if (optionalCarAvailabilityBefore.isPresent()) {   // case when only car availability before current reservation period is available

                    CarAvailability carAvBefore = optionalCarAvailabilityBefore.get();

                    carAvailabilityRepository.updateEndDate(endDate, carAvBefore.getId());
                    carAvailabilityRepository.deleteById(currentCarAvailability.getId());

                    if(newStartDate.isEqual(carAvailability.getStartDate()) && newEndDate.isEqual(carAvailability.getEndDate())) {

                        carAvailabilityRepository.updateStatus(carAvailability.getId(), "RESERVED");
                    } else if(newStartDate.isEqual(carAvailability.getStartDate())) {
                        carAvailabilityRepository.updateEndDateAndStatus(newEndDate, carAvailability.getId(), "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                    } else if(newEndDate.isEqual(carAvailability.getEndDate())) {
                        carAvailabilityRepository.updateStartDateAndStatus(newStartDate, carAvailability.getId(), "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                    } else {

                        carAvailabilityRepository.changePeriodAndStatus(carAvailability.getId(), newStartDate, newEndDate, "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                    }

                } else if (optionalCarAvailabilityAfter.isPresent()) {  // case when only car availability after current reservation period is available

                    CarAvailability carAvAfter = optionalCarAvailabilityAfter.get();

                    // if new reservation period is included in car availability right after current reservation period

                    if(carAvailability.getId() == carAvAfter.getId()) {

                        carAvailabilityRepository.updateEndDateAndStatus(newStartDate.minusDays(1), currentCarAvailability.getId(), "AVAILABLE");
                        carAvailabilityRepository.changePeriodAndStatus(carAvAfter.getId(), newStartDate, newEndDate, "RESERVED");

                        if(!newEndDate.isEqual(carAvAfter.getEndDate())) {
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1) , carAvAfter.getEndDate(), carAvailability.getCar()));
                        }
                    }
                   else {

                        carAvailabilityRepository.updateStartDate(currentCarAvailability.getStartDate(), carAvAfter.getId());
                        carAvailabilityRepository.deleteById(currentCarAvailability.getId());

                        if(newStartDate.isEqual(carAvailability.getStartDate()) && newEndDate.isEqual(carAvailability.getEndDate())) {

                            carAvailabilityRepository.updateStatus(carAvailability.getId(), "RESERVED");
                        } else if(newStartDate.isEqual(carAvailability.getStartDate())) {
                            carAvailabilityRepository.updateEndDateAndStatus(newEndDate, carAvailability.getId(), "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                        } else if(newEndDate.isEqual(carAvailability.getEndDate())) {
                            carAvailabilityRepository.updateStartDateAndStatus(newStartDate, carAvailability.getId(), "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                        } else {

                            carAvailabilityRepository.changePeriodAndStatus(carAvailability.getId(), newStartDate, newEndDate, "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                        }
                    }

                } else {
                    carAvailabilityRepository.updateStatus(currentCarAvailability.getId(), "AVAILABLE");

                    if(carAvailability.getStartDate().isEqual(newStartDate) && carAvailability.getEndDate().isEqual(newEndDate)) {
                        carAvailabilityRepository.updateStatus(carAvailability.getId(), "RESERVED");
                    } else if (carAvailability.getStartDate().isEqual(newStartDate)) {
                        carAvailabilityRepository.updateEndDateAndStatus(newEndDate, carAvailability.getId(), "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                    } else if (carAvailability.getEndDate().isEqual(newEndDate)) {
                        carAvailabilityRepository.updateStartDateAndStatus(newStartDate, carAvailability.getId(), "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                    } else {
                        carAvailabilityRepository.changePeriodAndStatus(carAvailability.getId(), newStartDate, newEndDate, "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                    }
                }
            } else {    // case when new period is before old one

                // if car is available after and before current reservation period
                if (optionalCarAvailabilityBefore.isPresent() && optionalCarAvailabilityAfter.isPresent()) {

                    CarAvailability carAvBefore = optionalCarAvailabilityBefore.get();
                    CarAvailability carAvAfter = optionalCarAvailabilityAfter.get();

                    // if new reservation period is included in car availability right after current reservation period
                    if(carAvailability.getId() == carAvBefore.getId()) {
                        carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvAfter.getId());
                        carAvailabilityRepository.deleteById(currentCarAvailability.getId());

                        carAvailabilityRepository.changePeriodAndStatus(carAvBefore.getId(), newStartDate, newEndDate, "RESERVED");

                        if(!newStartDate.isEqual(carAvBefore.getStartDate())) {
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvBefore.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                        }

                    } else {

                        // making car available for old reservation period
                        carAvailabilityRepository.updateEndDate(carAvAfter.getEndDate(), carAvBefore.getId());
                        carAvailabilityRepository.deleteById(currentCarAvailability.getId());
                        carAvailabilityRepository.deleteById(carAvAfter.getId());

                        if(newStartDate.isEqual(carAvailability.getStartDate()) && newEndDate.isEqual(carAvailability.getEndDate())) {

                            carAvailabilityRepository.updateStatus(carAvailability.getId(), "RESERVED");

                        } else if(newStartDate.isEqual(carAvailability.getStartDate())) {
                            carAvailabilityRepository.updateEndDateAndStatus(newEndDate, carAvailability.getId(), "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                        } else if(newEndDate.isEqual(carAvailability.getEndDate())) {
                            carAvailabilityRepository.updateStartDateAndStatus(newStartDate, carAvailability.getId(), "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                        } else {

                            carAvailabilityRepository.changePeriodAndStatus(carAvailability.getId(), newStartDate, newEndDate, "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                        }
                    }

                } else if (optionalCarAvailabilityBefore.isPresent()) {   // case when only car availability before current reservation period is available

                    CarAvailability carAvBefore = optionalCarAvailabilityBefore.get();

                    // if new reservation period is included in car availability right after current reservation period
                    if(carAvailability.getId() == carAvBefore.getId()) {

                        carAvailabilityRepository.updateStartDateAndStatus(newEndDate.plusDays(1), currentCarAvailability.getId(), "AVAILABLE");
                        carAvailabilityRepository.changePeriodAndStatus(carAvBefore.getId(), newStartDate, newEndDate, "RESERVED");

                        if(!newStartDate.isEqual(carAvBefore.getStartDate())) {
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvBefore.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                        }

                    } else {

                        // making car available for old reservation period
                        carAvailabilityRepository.updateEndDate(currentCarAvailability.getEndDate(), carAvBefore.getId());
                        carAvailabilityRepository.deleteById(currentCarAvailability.getId());

                        if(newStartDate.isEqual(carAvailability.getStartDate()) && newEndDate.isEqual(carAvailability.getEndDate())) {

                            carAvailabilityRepository.updateStatus(carAvailability.getId(), "RESERVED");

                        } else if(newStartDate.isEqual(carAvailability.getStartDate())) {
                            carAvailabilityRepository.updateEndDateAndStatus(newEndDate, carAvailability.getId(), "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                        } else if(newEndDate.isEqual(carAvailability.getEndDate())) {
                            carAvailabilityRepository.updateStartDateAndStatus(newStartDate, carAvailability.getId(), "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                        } else {

                            carAvailabilityRepository.changePeriodAndStatus(carAvailability.getId(), newStartDate, newEndDate, "RESERVED");
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                            carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                        }
                    }
                } else if (optionalCarAvailabilityAfter.isPresent()) {  // case when only car availability after current reservation period is available

                    CarAvailability carAvAfter = optionalCarAvailabilityAfter.get();

                    carAvailabilityRepository.updateStartDate(currentCarAvailability.getStartDate(), carAvAfter.getId());
                    carAvailabilityRepository.deleteById(currentCarAvailability.getId());

                    if(newStartDate.isEqual(carAvailability.getStartDate()) && newEndDate.isEqual(carAvailability.getEndDate())) {
                        carAvailabilityRepository.updateStatus(carAvailability.getId(), "RESERVED");
                    } else if(newStartDate.isEqual(carAvailability.getStartDate())) {
                        carAvailabilityRepository.updateEndDateAndStatus(newEndDate, carAvailability.getId(), "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                    } else if(newEndDate.isEqual(carAvailability.getEndDate())) {
                        carAvailabilityRepository.updateStartDateAndStatus(newStartDate, carAvailability.getId(), "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                    } else {

                        carAvailabilityRepository.changePeriodAndStatus(carAvailability.getId(), newStartDate, newEndDate, "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                    }

                } else {

                    carAvailabilityRepository.updateStatus(currentCarAvailability.getId(), "AVAILABLE");

                    if(carAvailability.getStartDate().isEqual(newStartDate) && carAvailability.getEndDate().isEqual(newEndDate)) {
                        carAvailabilityRepository.updateStatus(carAvailability.getId(), "RESERVED");
                    } else if (carAvailability.getStartDate().isEqual(newStartDate)) {
                        carAvailabilityRepository.updateEndDateAndStatus(newEndDate, carAvailability.getId(), "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                    } else if (carAvailability.getEndDate().isEqual(newEndDate)) {
                        carAvailabilityRepository.updateStartDateAndStatus(newStartDate, carAvailability.getId(), "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                    } else {
                        carAvailabilityRepository.changePeriodAndStatus(carAvailability.getId(), newStartDate, newEndDate, "RESERVED");
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), carAvailability.getEndDate(), carAvailability.getCar()));
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", carAvailability.getStartDate(), newStartDate.minusDays(1), carAvailability.getCar()));
                    }
                }
            }
        } else {

            CarAvailability currentCarAvailability = carAvailabilityRepository.getCarAvailability(carId, startDate, endDate)
                    .orElseThrow( () -> new NoSuchElementException("Car availability not found"));

            if(newStartDate.isEqual(endDate)){

                carAvailabilityRepository.changePeriod(currentCarAvailability.getId(), newStartDate, newEndDate);

                Optional<CarAvailability> carAvailabilityOptional = carAvailabilityRepository.checkStatusByEndDate(carId, startDate.minusDays(1), "AVAILABLE");

                if(carAvailabilityOptional.isPresent()) {
                    carAvailabilityRepository.updateEndDate(newStartDate.minusDays(1), carAvailabilityOptional.get().getId());
                } else {
                    carAvailabilityRepository.save(new CarAvailability("AVAILABLE", startDate, newStartDate.minusDays(1), carAvailability.getCar()));
                }

                if(carAvailability.getEndDate().isEqual(newEndDate)){
                    carAvailabilityRepository.deleteById(carAvailability.getId());
                } else {
                    carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailability.getId());
                }

            } else if (newEndDate.isEqual(startDate)) {

                carAvailabilityRepository.changePeriod(currentCarAvailability.getId(), newStartDate, newEndDate);

                Optional<CarAvailability> carAvailabilityOptional = carAvailabilityRepository.checkStatusByStartDate(carId, endDate.plusDays(1), "AVAILABLE");

                if(carAvailabilityOptional.isPresent()) {
                    carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailabilityOptional.get().getId());
                } else {
                    carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), endDate, carAvailability.getCar()));
                }

                if(carAvailability.getStartDate().isEqual(newStartDate)) {
                    carAvailabilityRepository.deleteById(carAvailability.getId());
                } else {
                    carAvailabilityRepository.updateEndDate(newStartDate.minusDays(1), carAvailability.getId());
                }
            } else if(startDate.isEqual(newStartDate)){

                carAvailabilityRepository.updateEndDate(newEndDate, currentCarAvailability.getId());

                if(carAvailability.getEndDate().isEqual(newEndDate)) {
                    carAvailabilityRepository.deleteById(carAvailability.getId());
                } else {
                    carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailability.getId());
                }
            } else if(endDate.isEqual(newEndDate)){

                carAvailabilityRepository.updateStartDate(newStartDate, currentCarAvailability.getId());

                if(carAvailability.getStartDate().isEqual(newStartDate))
                {
                    carAvailabilityRepository.deleteById(carAvailability.getId());
                } else {
                    carAvailabilityRepository.updateEndDate(newStartDate.minusDays(1), carAvailability.getId());
                }

            } else{

                if(newStartDate.isAfter(startDate) && newStartDate.isBefore(endDate)) {

                    carAvailabilityRepository.changePeriod(currentCarAvailability.getId(), newStartDate, newEndDate);

                    Optional<CarAvailability> carAvailabilityOptional = carAvailabilityRepository.checkStatusByEndDate(carId, startDate.minusDays(1), "AVAILABLE");

                    if(carAvailabilityOptional.isPresent()) {
                        carAvailabilityRepository.updateEndDate(newStartDate.minusDays(1), carAvailabilityOptional.get().getId());
                    } else {
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", startDate, newStartDate.minusDays(1), carAvailability.getCar()));
                    }

                    carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailability.getId());
                } else {

                    carAvailabilityRepository.changePeriod(currentCarAvailability.getId(), newStartDate, newEndDate);

                    Optional<CarAvailability> carAvailabilityOptional = carAvailabilityRepository.checkStatusByStartDate(carId, endDate.plusDays(1), "AVAILABLE");

                    if(carAvailabilityOptional.isPresent()) {
                        carAvailabilityRepository.updateStartDate(newEndDate.plusDays(1), carAvailabilityOptional.get().getId());
                    } else {
                        carAvailabilityRepository.save(new CarAvailability("AVAILABLE", newEndDate.plusDays(1), endDate, carAvailability.getCar()));
                    }

                    carAvailabilityRepository.updateEndDate(newStartDate.minusDays(1), carAvailability.getId());
                }
            }
        }
    }

    @Override
    public void makeCarAvailable(int carId, LocalDate startDate, LocalDate endDate) {

        CarAvailability carAvCurrent = carAvailabilityRepository.getCarAvailability(carId, startDate, endDate)
                .orElseThrow( () -> new NoSuchElementException("Car availability with car id: " + carId + " start date: " + startDate +
                         " end date: " + endDate + " not found"));

        Optional<CarAvailability> optionalAfterCarAv = carAvailabilityRepository.checkStatusByStartDate(carId, endDate.plusDays(1), "AVAILABLE");
        Optional<CarAvailability> optionalBeforeCarAv = carAvailabilityRepository.checkStatusByEndDate(carId, startDate.minusDays(1), "AVAILABLE");

        if(optionalAfterCarAv.isPresent() && optionalBeforeCarAv.isPresent()) {

            CarAvailability carAvBefore = optionalBeforeCarAv.get();
            CarAvailability carAvAfter = optionalAfterCarAv.get();

            carAvailabilityRepository.updateStartDate(carAvBefore.getStartDate(), carAvAfter.getId());
            carAvailabilityRepository.deleteById(carAvCurrent.getId());
            carAvailabilityRepository.deleteById(carAvBefore.getId());
        } else if (optionalAfterCarAv.isPresent()) {
            CarAvailability carAvAfter = optionalAfterCarAv.get();

            carAvailabilityRepository.updateStartDate(carAvCurrent.getStartDate(), carAvAfter.getId());
            carAvailabilityRepository.deleteById(carAvCurrent.getId());
        } else if (optionalBeforeCarAv.isPresent()) {
            CarAvailability carAvBefore = optionalBeforeCarAv.get();

            carAvailabilityRepository.updateStartDateAndStatus(carAvBefore.getStartDate(), carAvCurrent.getId(), "AVAILABLE");
            carAvailabilityRepository.deleteById(carAvBefore.getId());
        } else {
            carAvailabilityRepository.updateStatus(carAvCurrent.getId(), "AVAILABLE");
        }
    }
}
