package pl.myproject.car_rental_api.integration.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import pl.myproject.car_rental_api.dto.CarDTO;
import pl.myproject.car_rental_api.dto.EngineDTO;
import pl.myproject.car_rental_api.dto.GearboxDTO;
import pl.myproject.car_rental_api.dto.ModelDTO;
import pl.myproject.car_rental_api.service.CarService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("CarService integration tests")
public class CarServiceIntegrationTest {

    @Autowired
    private CarService carService;

    @Test
    @Order(1)
    @DisplayName("Test1: Saving Car entity after mapping from CarDTO")
    @Rollback(value = false)
    public void saveCar(){
        // defining DTO objects
        EngineDTO engineDTO = EngineDTO.builder()
                .capacity(new BigDecimal("2.0"))
                .horsepower(150)
                .torque(300)
                .fuelType("Diesel")
                .cylinderConfiguration("Inline")
                .engineType("Turbocharged")
                .build();

        GearboxDTO gearboxDTO = GearboxDTO.builder()
                .name("ZF6HP")
                .producer("ZF")
                .numberOfGears(6)
                .type("Automatic")
                .build();

        ModelDTO modelDTO = ModelDTO.builder()
                .name("BMW 320d")
                .type("Limousine")
                .productionYear(LocalDate.of(2020, 1, 1))
                .brand("BMW")
                .brandCountry("Germany")
                .color("Black")
                .typeOfDrive("FWD")
                .numberOfDoors(4)
                .bodyType("Sedan")
                .numberOfSeats(5)
                .environmentalLabel("Euro 6")
                .fuelConsumption(new BigDecimal("6"))
                .CO2Emissions(new BigDecimal("120"))
                .weight(1500)
                .accelerationTime(new BigDecimal("8.5"))
                .photoUrl("https://example.com/photo1.jpg")
                .averagePrice(30000)
                .description("A comfortable sedan with modern technologies")
                .engine(engineDTO)
                .gearbox(gearboxDTO)
                .build();

        CarDTO carDTO = CarDTO.builder()
                .registrationNumber("ABC12345")
                .lastServiceDate(LocalDate.of(2023, 6, 15))
                .mileage(50000)
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .rentalPricePerDay(100)
                .basePrice(25000)
                .model(modelDTO)
                .build();

        // calling saveCar() method on carService
        CarDTO result = carService.saveCar(carDTO);

        // checking data correctness
        assertThat(result).isNotNull();
        assertThat(result.getRegistrationNumber()).isEqualTo("ABC12345");
        assertThat(result.getModel().getBrand()).isEqualTo("BMW");
        assertThat(result.getModel().getEngine().getTorque()).isEqualTo(300);
        assertThat(result.getModel().getGearbox().getType()).isEqualTo("Automatic");

        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");

        System.out.println("CarDTO: " + result);
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Getting CarDTO by id")
    public void getCarDTOById() {

        // calling service method
        int id = 4;
        CarDTO carDTO = carService.getCarByIdWithDetails(id);

        // checking data correctness
        assertThat(carDTO).isNotNull();
        assertThat(carDTO.getId()).isEqualTo(id);
        assertThat(carDTO.getRegistrationNumber()).isEqualTo("ABC12345");
        assertThat(carDTO.getModel().getName()).isEqualTo("BMW 320d");
        assertThat(carDTO.getModel().getEngine().getFuelType()).isEqualTo("Diesel");
        assertThat(carDTO.getModel().getGearbox().getProducer()).isEqualTo("ZF");

        // printing out carDTO
        System.out.println("----------------------");
        System.out.println("----------------------");
        System.out.println("----------------------");
        System.out.println("CarDTO: " + carDTO);
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Deleting car by ID")
    public void deleteCarById() {

        // deleting car with given ID
        int id = 4;
        carService.deleteCarById(id);

        List<CarDTO> carDTOs = carService.getAllCarsWithDetails();

        CarDTO testCarDTO = carDTOs.stream()
                .filter( carDTO -> carDTO.getId() == id)
                .findFirst()
                .orElse(null);

        assertThat(testCarDTO).isNull();
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Retrieving Car entity and mapping them to DTO")
    public void getCarDTOs() {

        // getting cars DTO
        List<CarDTO> carDTOList = carService.getAllCarsWithDetails();

        // checking if list is not null
        assertThat(carDTOList).isNotNull();
        // checking if number of elements is correct
        assertThat(carDTOList.size()).isEqualTo(3);

        // getting a carDTO with given registration number
        CarDTO testCarDTO = carDTOList.stream()
                .filter(car -> car.getRegistrationNumber().equals("LMN9876"))
                        .findFirst()
                                .orElse(null);

        // checking carDTO data correctness
        assertThat(testCarDTO).isNotNull();
        assertThat(testCarDTO.getMileage()).isEqualTo(12000);
        assertThat(testCarDTO.getModel().getBodyType()).isEqualTo("Hatchback");
        assertThat(testCarDTO.getModel().getEngine().getTorque()).isEqualTo(200);
        assertThat(testCarDTO.getModel().getGearbox().getProducer()).isEqualTo("ZF");

        // displaying car dto
        System.out.println("-------------------------------------------------------------------------------------------");
        for (CarDTO carDTO : carDTOList) {
            System.out.println();
            System.out.println("CarDTO: " + carDTO);
        }
    }
}
