package pl.myproject.car_rental_api.unit.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.entity.CarAvailability;
import pl.myproject.car_rental_api.repository.CarAvailabilityRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CarAvailabilityRepositoryTest {

    @Autowired
    private CarAvailabilityRepository carAvailabilityRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Order(1)
    @DisplayName("Test 1: getting availability list by car id")
    public void getCarAvailabilityList(){

        // getting car availability by car id
        long carId = 1;
        List<CarAvailability> carAvailabilities = carAvailabilityRepository.findAllByCarId(carId);

        // checking data correctness
        assertThat(carAvailabilities.size()).isEqualTo(1);
        assertThat(carAvailabilities.get(0).getStatus()).isEqualTo("AVAILABLE");
        assertThat(carAvailabilities.get(0).getCar().getId()).isEqualTo(1);
        assertThat(carAvailabilities.get(0).getStartDate()).isEqualTo("2025-01-01");
        assertThat(carAvailabilities.get(0).getEndDate()).isEqualTo("2025-01-30");

        System.out.println("-----------------");
        System.out.println("-----------------");
        System.out.println("-----------------");

        // printing data
        int i = 1;
        for (CarAvailability carAv : carAvailabilities) {
            System.out.println(i + ". Car Availability[from: " + carAv.getStartDate() + " to: " + carAv.getEndDate() +
                    " status: " + carAv.getStatus() + " car id: " + carAv.getCar().getId());
            i++;
        }
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Check if car available in given date")
    public void checkIfCarAvailable() {

        // defining reservation period
        LocalDate from = LocalDate.of(2025, 01, 15);
        LocalDate to = LocalDate.of(2025, 01, 16);

        // checking if car is available for given period
        long carId = 1;
        CarAvailability carAvailability = carAvailabilityRepository.isCarAvailable(carId, from, to)
                .orElseThrow( () -> new NoSuchElementException("Car with id: " + carId + " is not available between: " + from + " and " + to));

        // checking data correctness
        assertThat(carAvailability.getStartDate()).isEqualTo(LocalDate.of(2025, 01, 01));
        assertThat(carAvailability.getEndDate()).isEqualTo(LocalDate.of(2025, 01, 30));
        assertThat(carAvailability.getStatus()).isEqualTo("AVAILABLE");
        assertThat(carAvailability.getCar().getId()).isEqualTo(carId);

        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("Car is available between: " + from + " and " + to);
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Updating end date with new date")
    @Rollback(value = false)
    public void updateEndDate() {

        // updating end date with new date
        int carAvailabilityId = 1;
        LocalDate newEndDate = LocalDate.of(2025, 01,14);
        carAvailabilityRepository.updateEndDate(newEndDate, carAvailabilityId);

        long carId = 1;
        List<CarAvailability> carAvailabilities = carAvailabilityRepository.findAllByCarId(carId);

        // retrieving car availability with entityManger
        CarAvailability carAv = entityManager.find(CarAvailability.class, carAvailabilityId);

        // checking data correctness
        assertThat(carAv.getEndDate()).isEqualTo(newEndDate);
        assertThat(carAv.getStartDate()).isEqualTo(LocalDate.of(2025, 01, 01));
        assertThat(carAv.getStatus()).isEqualTo("AVAILABLE");
//        assertThat(carAvailabilities.size()).isEqualTo(1);

        // printing out data
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("Car status: " + carAv.getStatus() + " start date: " + carAv.getStartDate() +
                " end date: " + carAv.getEndDate());

        System.out.println(carAvailabilities);
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: adding availability (reserved status)")
    @Rollback(value = false)
    public void savingAvailabilityForCar() {

        int carId = 1;
        LocalDate from = LocalDate.of(2025, 01, 15);
        LocalDate to = LocalDate.of(2025, 01, 16);

        Car car = entityManager.find(Car.class, carId);

        // creating car availability object to be saved
        CarAvailability carAvailability = CarAvailability.builder()
                .status("RESERVED")
                .startDate(from)
                .endDate(to)
                .car(car)
                .build();

        // saving new car availability
        CarAvailability newCarAvailability = carAvailabilityRepository.save(carAvailability);

        // getting all car availabilities for given car
        List<CarAvailability> carAvailabilities = carAvailabilityRepository.findAllByCarId(carId);

        // getting availability with reserved status if exists
        CarAvailability carAvailability1 = carAvailabilities.stream()
                .filter( carAv -> carAv.getStatus().equalsIgnoreCase("reserved"))
                .findFirst()
                .orElse(null);

        // checking if it exists (has been successfully saved)
        assertThat(carAvailability1).isNotNull();

        System.out.println("----------------------------");
        System.out.println("----------------------------");
        System.out.println("----------------------------");
        // printing data
        int i = 1;
        for (CarAvailability carAv : carAvailabilities) {
            System.out.println(i + ". Car Availability[from: " + carAv.getStartDate() + " to: " + carAv.getEndDate() +
                    " status: " + carAv.getStatus() + " car id: " + carAv.getCar().getId());
            i++;
        }
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: saving new car availability")
    @Rollback(value = false)
    public void savingNewCarAvailable() {

        int carId = 1;
        LocalDate from = LocalDate.of(2025, 01, 17);
        LocalDate to = LocalDate.of(2025, 01, 30);

        Car car = entityManager.find(Car.class, carId);

        // creating car availability object to be saved
        CarAvailability carAvailability = CarAvailability.builder()
                .status("AVAILABLE")
                .startDate(from)
                .endDate(to)
                .car(car)
                .build();

        // saving new car availability
        CarAvailability newCarAvailability = carAvailabilityRepository.save(carAvailability);

        // getting all car availabilities for given car
        List<CarAvailability> carAvailabilities = carAvailabilityRepository.findAllByCarId(carId);

        List<CarAvailability> availablePeriods = carAvailabilities.stream()
                        .filter( carAv -> carAv.getStatus().equalsIgnoreCase("available"))
                                .toList();

        assertThat(availablePeriods.size()).isEqualTo(2);
//        assertThat(carAvailabilities.size()).isEqualTo(3);
        System.out.println("----------------------------");
        System.out.println("----------------------------");
        System.out.println("----------------------------");
        // printing data
        int i = 1;
        for (CarAvailability carAv : carAvailabilities) {
            System.out.println(i + ". Car Availability[from: " + carAv.getStartDate() + " to: " + carAv.getEndDate() +
                    " status: " + carAv.getStatus() + " car id: " + carAv.getCar().getId());
            i++;
        }

    }
}
