package pl.myproject.car_rental_api.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.myproject.car_rental_api.controller.CarController;
import pl.myproject.car_rental_api.dto.CarDTO;
import pl.myproject.car_rental_api.dto.EngineDTO;
import pl.myproject.car_rental_api.dto.GearboxDTO;
import pl.myproject.car_rental_api.dto.ModelDTO;
import pl.myproject.car_rental_api.service.CarService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;

@WebMvcTest(CarController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("CarController unit tests")
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("Test 1: saving new Car")
    public void savingCar() throws Exception{

        // defining DTO objects which will be returned by service save() method
        EngineDTO engineDTO1 = EngineDTO.builder()
                .id(1)
                .capacity(new BigDecimal("2.0"))
                .horsepower(150)
                .torque(300)
                .fuelType("Diesel")
                .cylinderConfiguration("Inline")
                .engineType("Turbocharged")
                .build();

        GearboxDTO gearboxDTO1 = GearboxDTO.builder()
                .id(1)
                .name("ZF6HP")
                .producer("ZF")
                .numberOfGears(6)
                .type("Automatic")
                .build();

        ModelDTO modelDTO1 = ModelDTO.builder()
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
                .engine(engineDTO1)
                .gearbox(gearboxDTO1)
                .build();

        CarDTO carDTO1 = CarDTO.builder()
                .id(1)
                .registrationNumber("ABC12345")
                .vin("WBA3D31000F300276")
                .lastServiceDate(LocalDate.of(2023, 6, 15))
                .mileage(50000)
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .rentalPricePerDay(100)
                .basePrice(25000)
                .model(modelDTO1)
                .build();

        // defining DTO object, that will be passed to controller method which handle POST request
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
                .vin("WBA3D31000F300276")
                .lastServiceDate(LocalDate.of(2023, 6, 15))
                .mileage(50000)
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .rentalPricePerDay(100)
                .basePrice(25000)
                .model(modelDTO)
                .build();

        // mocking carService.saveCar() behaviour to return carDTO with id value
        given(carService.saveCar(any(CarDTO.class))).willReturn(carDTO1);

        // sending POST request to "/api/cars"
        ResultActions result = mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carDTO)));

        // checking data correctness
        result.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.registrationNumber", is("ABC12345")))
                .andExpect(jsonPath("$.model.brand", is("BMW")))
                .andExpect(jsonPath("$.model.engine.fuelType", is("Diesel")))
                .andExpect(jsonPath("$.model.gearbox.type", is("Automatic")));
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Getting CarDTO by ID")
    public void getCarDTO() throws Exception{

        int i = 1;

        // defining DTO objects which will be returned by service findCarByIdWithDetails() method
        EngineDTO engineDTO = EngineDTO.builder()
                .id(i)
                .capacity(new BigDecimal("2.0"))
                .horsepower(150)
                .torque(300)
                .fuelType("Diesel")
                .cylinderConfiguration("Inline")
                .engineType("Turbocharged")
                .build();

        GearboxDTO gearboxDTO = GearboxDTO.builder()
                .id(i)
                .name("ZF6HP")
                .producer("ZF")
                .numberOfGears(6)
                .type("Automatic")
                .build();

        ModelDTO modelDTO = ModelDTO.builder()
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
                .engine(engineDTO)
                .gearbox(gearboxDTO)
                .build();

        CarDTO carDTO = CarDTO.builder()
                .id(i)
                .registrationNumber("ABC12345")
                .vin("WBA3D31000F300276")
                .lastServiceDate(LocalDate.of(2023, 6, 15))
                .mileage(50000)
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .rentalPricePerDay(100)
                .basePrice(25000)
                .model(modelDTO)
                .build();

        // mocking carService.getCarByIdWithDetails() behaviour to return carDTO
        given(carService.getCarByIdWithDetails(i)).willReturn(carDTO);

        // sending GET request to "/api/cars/{id}"
        ResultActions response = mockMvc.perform(get("/api/cars/{id}", i));

        // checking data correctness
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(i)))
                .andExpect(jsonPath("$.registrationNumber", is("ABC12345")))
                .andExpect(jsonPath("$.model.color", is("Black")))
                .andExpect(jsonPath("$.model.engine.fuelType", is("Diesel")))
                .andExpect(jsonPath("$.model.gearbox.type", is("Automatic")));
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Deleting Car by ID")
    public void deletingCarById() throws Exception{

        int id = 3;
        // mocking carService.deleteCarById() method
        willDoNothing().given(carService).deleteCarById(id);

        // sending DELETE request to "/api/cars/{id}"
        ResultActions response = mockMvc.perform(delete("/api/cars/{id}", id));

        // checking data correctness
        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: getting carDTOs")
    public void getCarsDTO() throws Exception{

        // creating test data
        // creating Engine objects
        EngineDTO engineDTO1 = EngineDTO.builder()
                .id(1)
                .capacity(new BigDecimal("2.0"))
                .horsepower(150)
                .torque(300)
                .fuelType("Diesel")
                .cylinderConfiguration("Inline")
                .engineType("Turbocharged")
                .build();

        EngineDTO engineDTO2 = EngineDTO.builder()
                .id(2)
                .capacity(new BigDecimal("1.6"))
                .horsepower(120)
                .torque(250)
                .fuelType("Petrol")
                .cylinderConfiguration("Inline")
                .engineType("Naturally Aspirated")
                .build();

        // creating Gearbox objects
        GearboxDTO gearboxDTO1 = GearboxDTO.builder()
                .id(1)
                .name("ZF6HP")
                .producer("ZF")
                .numberOfGears(6)
                .type("Automatic")
                .build();

        GearboxDTO gearboxDTO2 = GearboxDTO.builder()
                .id(2)
                .name("Getrag5MT")
                .producer("Getrag")
                .numberOfGears(5)
                .type("Manual")
                .build();

        // creating Model objects
        ModelDTO modelDTO1 = ModelDTO.builder()
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
                .engine(engineDTO1)
                .gearbox(gearboxDTO1)
                .build();

        ModelDTO modelDTO2 = ModelDTO.builder()
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
                .engine(engineDTO2)
                .gearbox(gearboxDTO2)
                .build();

        // creating Car objects
        CarDTO carDTO1 = CarDTO.builder()
                .id(1)
                .registrationNumber("ABC12345")
                .vin("WBA3D31000F300276")
                .lastServiceDate(LocalDate.of(2023, 6, 15))
                .mileage(50000)
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .rentalPricePerDay(100)
                .basePrice(25000)
                .model(modelDTO1)
                .build();

        CarDTO carDTO2 = CarDTO.builder()
                .id(2)
                .registrationNumber("XYZ67890")
                .vin("VNKVNKVNK45696969")
                .lastServiceDate(LocalDate.of(2022, 8, 20))
                .mileage(30000)
                .insuranceExpiryDate(LocalDate.of(2024, 10, 30))
                .rentalPricePerDay(80)
                .basePrice(15000)
                .model(modelDTO2)
                .build();

        // carService will return list of cars when invoking getAllCarsWithDetails() method
        given(carService.getAllCarsWithDetails()).willReturn(List.of(carDTO1, carDTO2));

        // Get request simulation
        ResultActions response = mockMvc.perform(get("/api/cars"));

        // checking returned data correctness
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].registrationNumber", is("XYZ67890")))
                .andExpect(jsonPath("$[1].model.brand", is("Toyota")))
                .andExpect(jsonPath("$[1].model.engine.fuelType", is("Petrol")))
                .andExpect(jsonPath("$[1].model.gearbox.type", is("Manual")));
    }
}
