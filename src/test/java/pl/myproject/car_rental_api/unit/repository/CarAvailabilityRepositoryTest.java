package pl.myproject.car_rental_api.unit.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
    @DisplayName("Test 2: Check is car available in given date")
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
}
