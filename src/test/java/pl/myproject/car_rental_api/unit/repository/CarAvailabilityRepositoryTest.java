package pl.myproject.car_rental_api.unit.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.myproject.car_rental_api.entity.CarAvailability;
import pl.myproject.car_rental_api.repository.CarAvailabilityRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CarAvailabilityRepositoryTest {

    @Autowired
    private CarAvailabilityRepository carAvailabilityRepository;

    @Test
    @Order(1)
    @DisplayName("Test 1: getting availability list by car id")
    public void getCarAvailabilityList(){

        long carId = 1;
        List<CarAvailability> carAvailabilities = carAvailabilityRepository.findAllByCarId(carId);

        assertThat(carAvailabilities.size()).isEqualTo(1);
        assertThat(carAvailabilities.get(0).getStatus()).isEqualTo("AVAILABLE");
        assertThat(carAvailabilities.get(0).getCar().getId()).isEqualTo(1);
        assertThat(carAvailabilities.get(0).getFrom()).isEqualTo("2025-01-01");
        assertThat(carAvailabilities.get(0).getTo()).isEqualTo("2025-01-30");

        System.out.println("-----------------");
        System.out.println("-----------------");
        System.out.println("-----------------");

        int i = 1;
        for (CarAvailability carAv : carAvailabilities) {
            System.out.println(i + ". Car Availability[from: " + carAv.getFrom() + " to: " + carAv.getTo() +
                    " status: " + carAv.getStatus() + " car id: " + carAv.getCar().getId());
            i++;
        }
    }
}
