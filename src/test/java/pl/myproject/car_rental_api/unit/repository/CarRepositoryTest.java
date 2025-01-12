package pl.myproject.car_rental_api.unit.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.entity.Engine;
import pl.myproject.car_rental_api.entity.Gearbox;
import pl.myproject.car_rental_api.entity.Model;
import pl.myproject.car_rental_api.repository.CarRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("CarRepository unit tests")
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Order(1)
    @DisplayName("Test1: Saving Car entity with related entities")
    @Rollback(value = false)
    public void insertingCarWithDetails() {

        // creating test data
        // creating Engine objects
        Engine engine = Engine.builder()
                .capacity(new BigDecimal("4.0"))
                .horsepower(592)
                .torque(800)
                .fuelType("Petrol")
                .cylinderConfiguration("V8")
                .engineType("Naturally-aspirated")
                .build();

        // creating Gearbox objects
        Gearbox gearbox = Gearbox.builder()
                .name("ZF8HP90")
                .producer("ZF")
                .numberOfGears(8)
                .type("Automatic")
                .build();

        // creating Model objects
        Model model = Model.builder()
                .name("Audi RS6")
                .type("Sport car")
                .productionYear(LocalDate.of(2019, 1, 1))
                .brand("Audi")
                .brandCountry("Germany")
                .color("Black")
                .typeOfDrive("AWD")
                .numberOfDoors(4)
                .bodyType("Avant")
                .numberOfSeats(5)
                .environmentalLabel("Euro 6")
                .fuelConsumption(new BigDecimal("12.4"))
                .CO2Emissions(new BigDecimal("268"))
                .weight(2075)
                .accelerationTime(new BigDecimal("3.6"))
                .photoUrl("https://example.com/photo3.jpg")
                .averagePrice(30000)
                .description("A comfortable and super fast family car")
                .engine(engine)
                .gearbox(gearbox)
                .build();

        // creating Car objects
        Car car = Car.builder()
                .registrationNumber("QWER123")
                .lastServiceDate(LocalDate.of(2023, 6, 15))
                .mileage(50000)
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .rentalPricePerDay(100)
                .basePrice(25000)
                .model(model)
                .build();

        // saving car entity with related entities
        Car testCar = carRepository.save(car);

        // retrieving saved car entity
        Car retrievedCar = entityManager.find(Car.class, testCar.getId());

        // checking if related entities exists
        assertThat(retrievedCar).isNotNull();
        assertThat(retrievedCar.getModel()).isNotNull();
        assertThat(retrievedCar.getModel().getEngine()).isNotNull();
        assertThat(retrievedCar.getModel().getGearbox()).isNotNull();

        // checking if related entities has been saved correctly
        assertThat(retrievedCar.getRegistrationNumber()).isEqualTo(testCar.getRegistrationNumber());
        assertThat(retrievedCar.getModel().getName()).isEqualTo(testCar.getModel().getName());
        assertThat(retrievedCar.getModel().getEngine().getCapacity()).isEqualTo(testCar.getModel().getEngine().getCapacity());
        assertThat(retrievedCar.getModel().getGearbox().getNumberOfGears()).isEqualTo(testCar.getModel().getGearbox().getNumberOfGears());

        System.out.println("-------------------------------------");
        System.out.println("-------------------------------------");
        System.out.println("-------------------------------------");

        System.out.println("Saved car: " + retrievedCar);
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Retrieving Car entity with related entities")
    public void getAllCarsWithDetails() {

        // retrieving list of cars with related engine and gearbix
        List<Car> carList = carRepository.getAllCarsWithDetails();

        // checking if carList is not null
        assertThat(carList).isNotNull();

        // checking if carList has 3 cars
        assertThat(carList).hasSize(4);

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
