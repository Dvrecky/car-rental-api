package pl.myproject.car_rental_api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.myproject.car_rental_api.entity.Car;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    public void getAllCarsWithDetails() {

        // retrieving list of cars with related engine and gearbix
        List<Car> carList = carRepository.getAllCarsWithDetails();

        // checking if carList is not null
        assertThat(carList).isNotNull();

        // checking if carList has 3 cars
        assertThat(carList).hasSize(3);

        // getting a car with given registration number
        Car testCar = carList.stream()
                        .filter(c -> c.getRegistrationNumber().equalsIgnoreCase("ABC1234"))
                                .findFirst()
                                        .orElse(null);

        // checking if given car exists
        assertThat(testCar).isNotNull();

        // checking if brand name for given car is correct
        assertThat(testCar.getModel().getBrand()).isEqualTo("BrandA");

        // checking if color for given car is correct
        assertThat(testCar.getModel().getColor()).isEqualTo("Red");

        // checking if engine type for given car is correct
        assertThat(testCar.getModel().getEngine().getEngineType()).isEqualTo("Turbocharged");

        // checking if number of gears for given car is correct
        assertThat(testCar.getModel().getGearbox().getNumberOfGears()).isEqualTo(6);

        // displaying all retrieved cars
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Car car : carList) {
            System.out.println("Car: " + car);
            System.out.println();
        }
    }
}
