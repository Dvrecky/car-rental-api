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
import pl.myproject.car_rental_api.projection.CarBaseInfoProjection;
import pl.myproject.car_rental_api.repository.CarRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    @DisplayName("Test 1: Saving Car entity with related entities")
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
                .vin("WUAZZZF21SN903325")
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
    @DisplayName("Test 2: finding car id")
    public void findCarById() {

        int id = 4;
        Car car = carRepository.findCarWithDetailsById(id).orElseThrow(() ->
            new IllegalStateException("Car with id: " + id + " not found")
        );

        assertThat(car.getId()).isEqualTo(4);
        assertThat(car.getRegistrationNumber()).isEqualTo("QWER123");
        assertThat(car.getModel().getBodyType()).isEqualTo("Avant");
        assertThat(car.getModel().getEngine().getCapacity()).isEqualTo("4.0");
        assertThat(car.getModel().getGearbox().getNumberOfGears()).isEqualTo(8);

        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------");
        System.out.println("Found car: " + car);

    }

    @Test
    @Order(3)
    @DisplayName("Test 3: find Car entity by VIN")
    public void findCarByVin() {

        String vin = "WUAZZZF21SN903325";
        Car car = carRepository.findCarByVin(vin)
                                    .orElseThrow( () -> new NoSuchElementException("Car not found for vin: " + vin));

        assertThat(car.getVin()).isEqualTo(vin);
        assertThat(car.getRegistrationNumber()).isEqualTo("QWER123");

        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("Car: " + car.toStringDetails());

    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Deleting Car entity by id")
    @Rollback(value = false)
    public void deletingCarById() {

        // removing car and related entities with given car id
        int id = 3;
        carRepository.deleteById(id);

        Optional<Car> carOptional = carRepository.findCarWithDetailsById(id);
        assertThat(carOptional).isEmpty();
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Updating Car entity")
    @Rollback(value = false)
    public void updateExistingCarEntity() {

        // creating test entities
        Engine engine = Engine.builder()
                .id(2)
                .capacity(new BigDecimal("2.0"))
                .horsepower(250)
                .torque(500)
                .fuelType("Diesel")
                .cylinderConfiguration("V6")
                .engineType("Naturally-aspirated")
                .build();

        Gearbox gearbox = Gearbox.builder()
                .id(3)
                .name("CVT")
                .producer("Jatco")
                .numberOfGears(8)
                .type("CVT")
                .build();

        Model model = Model.builder()
                .id(2)
                .name("Model B")
                .type("SUV")
                .productionYear(LocalDate.of(2021, 11, 15))
                .brand("BrandB")
                .brandCountry("USA")
                .color("Yellow")
                .typeOfDrive("AWD")
                .numberOfDoors(5)
                .bodyType("SUV")
                .numberOfSeats(7)
                .environmentalLabel("Euro 5")
                .fuelConsumption(new BigDecimal("8.2"))
                .CO2Emissions(new BigDecimal("200"))
                .weight(1800)
                .accelerationTime(new BigDecimal("10.2"))
                .photoUrl("https://example.com/photoB.jpg")
                .averagePrice(30000)
                .description("A family SUV offering great comfort and space.")
                .engine(engine)
                .gearbox(gearbox)
                .build();

        Car testCar = Car.builder()
                .id(2)
                .registrationNumber("XYZ5678")
                .vin("2C3KA53G76H654321")
                .lastServiceDate(LocalDate.of(2024, 11, 15))
                .mileage(20000) // było 18000
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .rentalPricePerDay(150)
                .basePrice(35000)
                .model(model)
                .build();

        // retrieving car with given ID
        int id = 2;
        Car car = carRepository.findCarWithDetailsById(id)
                                        .orElseThrow( () -> new NoSuchElementException("Car not found for ID: " + id));

        // printing Car data
        System.out.println("Car mileage before update: " + car.getMileage());
        System.out.println("Car color before update: " + car.getModel().getColor());
        System.out.println("Car engine capacity before update: " + car.getModel().getEngine().getCapacity());
        System.out.println("Car number of gears before update: " + car.getModel().getGearbox().getNumberOfGears());
        System.out.println("Car before update: " + car);

        // updating Car entity with related entities
        Car updatedCar = carRepository.save(testCar);

        // printing updated Car data
        System.out.println("Car mileage after update: " + updatedCar.getMileage());
        System.out.println("Car color after update: " + updatedCar.getModel().getColor());
        System.out.println("Car engine capacity after update: " + updatedCar.getModel().getEngine().getCapacity());
        System.out.println("Car number of gears after update: " + updatedCar.getModel().getGearbox().getNumberOfGears());

        // checking if Car and related entities has been updated correctly
        assertThat(updatedCar.getMileage()).isEqualTo(20000);
        assertThat(updatedCar.getModel().getColor()).isEqualTo("Yellow");
        assertThat(updatedCar.getModel().getEngine().getCapacity()).isEqualTo(BigDecimal.valueOf(2.0));
        assertThat(updatedCar.getModel().getGearbox().getNumberOfGears()).isEqualTo(8);

        // printing updated car
        System.out.println("----------------------------");
        System.out.println("----------------------------");
        System.out.println("----------------------------");
        System.out.println("Updated car: " + updatedCar);


    }

    @Test
    @Order(6)
    @DisplayName("Test 6: Retrieving Car entity with related entities")
    public void getAllCarsWithDetails() {

        // retrieving list of cars with related engine and gearbix
        List<Car> carList = carRepository.findAllCarsWithDetails();

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

    @Test
    @Order(7)
    @DisplayName("Test 7: Retrieving list of car base view")
    public void getListOfBaseCarView() {

//        List<CarBaseInfoProjection> baseInfoProjectionList = carRepository.findAllCarBaseInfo();
//
//        assertThat(baseInfoProjectionList.size()).isEqualTo(3);

    }


}
