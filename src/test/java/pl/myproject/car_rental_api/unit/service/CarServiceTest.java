package pl.myproject.car_rental_api.unit.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import pl.myproject.car_rental_api.dto.car.CarDetailsDTO;
import pl.myproject.car_rental_api.dto.engine.EngineDTO;
import pl.myproject.car_rental_api.dto.gearbox.GearboxDTO;
import pl.myproject.car_rental_api.dto.model.ModelDTO;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.entity.Engine;
import pl.myproject.car_rental_api.entity.Gearbox;
import pl.myproject.car_rental_api.entity.Model;
import pl.myproject.car_rental_api.repository.CarRepository;
import pl.myproject.car_rental_api.service.CarService;
import pl.myproject.car_rental_api.service.impl.CarServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("CarService unit tests")
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    private CarService carService;

    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {

        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        carService = new CarServiceImpl(carRepository, modelMapper);
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: Returning CarDTO after saving do database")
    public void ReturningCarDTOAfterSavingToDatabase() {

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

        CarDetailsDTO carDTO = CarDetailsDTO.builder()
                .registrationNumber("ABC12345")
                .vin("WBA3D31000F300276")
                .lastServiceDate(LocalDate.of(2023, 6, 15))
                .mileage(50000)
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .model(modelDTO)
                .build();

        // defining entities that will be returned by save() method
        Engine engine = Engine.builder()
                .id(1)
                .capacity(new BigDecimal("2.0"))
                .horsepower(150)
                .torque(300)
                .fuelType("Diesel")
                .cylinderConfiguration("Inline")
                .engineType("Turbocharged")
                .build();

        Gearbox gearbox = Gearbox.builder()
                .id(1)
                .name("ZF6HP")
                .producer("ZF")
                .numberOfGears(6)
                .type("Automatic")
                .build();

        Model model = Model.builder()
                .id(1)
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
                .engine(engine)
                .gearbox(gearbox)
                .build();

        Car car = Car.builder()
                .id(1)
                .registrationNumber("ABC12345")
                .vin("WBA3D31000F300276")
                .lastServiceDate(LocalDate.of(2023, 6, 15))
                .mileage(50000)
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .model(model)
                .build();

        // mocking carRepository.save() method behaviour
        given(carRepository.save(any(Car.class))).willReturn(car);

        // calling saveCar() method
        CarDetailsDTO result = carService.saveCar(carDTO);

        // checking data correctness
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getRegistrationNumber()).isEqualTo("ABC12345");
        assertThat(result.getModel().getName()).isEqualTo("BMW 320d");
        assertThat(result.getModel().getEngine().getHorsepower()).isEqualTo(150);

        System.out.println("-----------------");
        System.out.println("-----------------");
        System.out.println("-----------------");

        System.out.println("CarDTO: " + result);
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Getting CarDTO by id")
    public void returnCarDTOById() {

        int i = 1;

        // defining entities that will be returned by findCarByIdWithDetails() method
        Engine engine = Engine.builder()
                .id(i)
                .capacity(new BigDecimal("2.0"))
                .horsepower(150)
                .torque(300)
                .fuelType("Diesel")
                .cylinderConfiguration("Inline")
                .engineType("Turbocharged")
                .build();

        Gearbox gearbox = Gearbox.builder()
                .id(i)
                .name("ZF6HP")
                .producer("ZF")
                .numberOfGears(6)
                .type("Automatic")
                .build();

        Model model = Model.builder()
                .id(i)
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
                .engine(engine)
                .gearbox(gearbox)
                .build();

        Car car = Car.builder()
                .id(i)
                .registrationNumber("ABC12345")
                .vin("WBA3D31000F300276")
                .lastServiceDate(LocalDate.of(2023, 6, 15))
                .mileage(50000)
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .model(model)
                .build();

        // mocking carRepository behaviour to return car entity
        given(carRepository.findCarWithDetailsById(i)).willReturn(Optional.of(car));

        // calling service method
        CarDetailsDTO result = carService.getCarDTOWithDetailsById(i);

        // checking data correctness
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(i);
        assertThat(result.getRegistrationNumber()).isEqualTo("ABC12345");
        assertThat(result.getModel().getBrandCountry()).isEqualTo("Germany");
        assertThat(result.getModel().getEngine().getEngineType()).isEqualTo("Turbocharged");
        assertThat(result.getModel().getGearbox().getProducer()).isEqualTo("ZF");

        // printing out the result
        System.out.println("---------------------------------------");
        System.out.println("---------------------------------------");
        System.out.println("---------------------------------------");
        System.out.println("CarDTO: " + result);
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: delete Car by ID")
    public void deleteCarById() {

        // mocking carRepository behaviour
        int id = 1;
        willDoNothing().given(carRepository).deleteById(id);

        // calling service method for deleting
        carService.deleteCarById(id);

        // verifying if carRepository.deleteById() method has been invoked once
        verify(carRepository, times(1)).deleteById(id);
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Retrieving Car entity and mapping them to DTO")
    public void returnCarDTOs() {

        // creating test data
        // creating Engine objects
        Engine engine1 = Engine.builder()
                .id(1)
                .capacity(new BigDecimal("2.0"))
                .horsepower(150)
                .torque(300)
                .fuelType("Diesel")
                .cylinderConfiguration("Inline")
                .engineType("Turbocharged")
                .build();

        Engine engine2 = Engine.builder()
                .id(2)
                .capacity(new BigDecimal("1.6"))
                .horsepower(120)
                .torque(250)
                .fuelType("Petrol")
                .cylinderConfiguration("Inline")
                .engineType("Naturally Aspirated")
                .build();

        // creating Gearbox objects
        Gearbox gearbox1 = Gearbox.builder()
                .id(1)
                .name("ZF6HP")
                .producer("ZF")
                .numberOfGears(6)
                .type("Automatic")
                .build();

        Gearbox gearbox2 = Gearbox.builder()
                .id(2)
                .name("Getrag5MT")
                .producer("Getrag")
                .numberOfGears(5)
                .type("Manual")
                .build();

        // creating Model objects
        Model model1 = Model.builder()
                .id(1)
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
                .engine(engine1)
                .gearbox(gearbox1)
                .build();

        Model model2 = Model.builder()
                .id(2)
                .name("Toyota Yaris")
                .type("Urban")
                .productionYear(LocalDate.of(2019, 6, 15))
                .brand("Toyota")
                .brandCountry("Japan")
                .color("Red")
                .typeOfDrive("FWD")
                .numberOfDoors(5)
                .bodyType("Hatchback")
                .numberOfSeats(5)
                .environmentalLabel("Euro 6")
                .fuelConsumption(new BigDecimal("5"))
                .CO2Emissions(new BigDecimal("95"))
                .weight(1200)
                .accelerationTime(new BigDecimal("10.2"))
                .photoUrl("https://example.com/photo2.jpg")
                .averagePrice(20000)
                .description("An economical hatchback perfect for the city")
                .engine(engine2)
                .gearbox(gearbox2)
                .build();

        // creating Car objects
        Car car1 = Car.builder()
                .id(1)
                .registrationNumber("ABC12345")
                .vin("WBA3D31000F300276")
                .lastServiceDate(LocalDate.of(2023, 6, 15))
                .mileage(50000)
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .model(model1)
                .build();

        Car car2 = Car.builder()
                .id(2)
                .registrationNumber("XYZ67890")
                .vin("VNKVNKVNK45696969")
                .lastServiceDate(LocalDate.of(2022, 8, 20))
                .mileage(30000)
                .insuranceExpiryDate(LocalDate.of(2024, 10, 30))
                .model(model2)
                .build();

        // carRepository will return list of car1 and car2 when invoking getAllCarsWithDetails method
        given(carRepository.findAllCarsWithDetails()).willReturn(List.of(car1, car2));

        // retrieving list of cars with service
        List<CarDetailsDTO> carDTOs = carService.getAllCarsWithDetails();

        assertThat(carDTOs).isNotNull();
        assertThat(carDTOs.size()).isEqualTo(2);

        // getting carDTO with given data
        CarDetailsDTO testCarDTO = carDTOs.stream()
                .filter(car -> car.getRegistrationNumber().equals("ABC12345"))
                .findFirst()
                .orElse(null);

        // checking if car exists
        assertThat(testCarDTO).isNotNull();
        // checking if data has been mapped correctly
        assertThat(testCarDTO.getModel().getBrand()).isEqualTo("BMW");
        assertThat(testCarDTO.getModel().getEngine().getEngineType()).isEqualTo("Turbocharged");
        assertThat(testCarDTO.getModel().getGearbox().getProducer()).isEqualTo("ZF");

        // displaying carDTOs list
        System.out.println("-------------------------------------------------------------------------------");
        for(CarDetailsDTO carDTO : carDTOs) {
            System.out.println();
            System.out.println("CarDTO: " + carDTO);
        }
    }
}
