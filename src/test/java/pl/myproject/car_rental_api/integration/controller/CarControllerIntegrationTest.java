package pl.myproject.car_rental_api.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.myproject.car_rental_api.dto.car.CarDetailsDTO;
import pl.myproject.car_rental_api.dto.engine.EngineDTO;
import pl.myproject.car_rental_api.dto.gearbox.GearboxDTO;
import pl.myproject.car_rental_api.dto.model.ModelDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("CarController integration tests")
public class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("Test 1: Saving Car")
    @Rollback(value = false)
    public void saveCar() throws Exception{

        // creating test CarDTO that will be saved
        EngineDTO engineDTO = EngineDTO.builder()
                .capacity(new BigDecimal("4.0"))
                .horsepower(592)
                .torque(800)
                .fuelType("Petrol")
                .cylinderConfiguration("V8")
                .engineType("Naturally-aspirated")
                .build();

        GearboxDTO gearboxDTO = GearboxDTO.builder()
                .name("ZF8HP90")
                .producer("ZF")
                .numberOfGears(8)
                .type("Automatic")
                .build();

        ModelDTO modelDTO = ModelDTO.builder()
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
                .engine(engineDTO)
                .gearbox(gearboxDTO)
                .build();

        CarDetailsDTO carDTO = CarDetailsDTO.builder()
                .registrationNumber("QWER123")
                .vin("WUAZZZF21SN903325")
                .lastServiceDate(LocalDate.of(2023, 6, 15))
                .mileage(50000)
                .insuranceExpiryDate(LocalDate.of(2025, 12, 31))
                .model(modelDTO)
                .build();

        // sending POST request to "/api/cars"
        ResultActions response = mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carDTO)));

        // checking data correctness
        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.registrationNumber", is("QWER123")))
                .andExpect(jsonPath("$.model.brand", is("Audi")))
                .andExpect(jsonPath("$.model.engine.horsepower", is(592)))
                .andExpect(jsonPath("$.model.gearbox.name" , is("ZF8HP90")));
    }

    @Test
    @Order(2)
    @DisplayName("Test 3: Getting Car by ID and mapping to DTO")
    public void getCarDTOById() throws Exception{

        int i = 4;
        // sending GET request to "/api/cars/{id}"
        ResultActions response = mockMvc.perform(get("/api/cars/{i}", i));

        // checking data correctness
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(i)))
                .andExpect(jsonPath("$.registrationNumber", is("QWER123")))
                .andExpect(jsonPath("$.model.brandCountry", is("Germany")))
                .andExpect(jsonPath("$.model.engine.torque", is(800)))
                .andExpect(jsonPath("$.model.gearbox.type" , is("Automatic")));
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Deleting car by ID")
    public void deletingCarById() throws Exception{

        int id = 4;
        // sending DELETE request to "/api/cars/{id}"
        ResultActions response = mockMvc.perform(delete("/api/cars/{id}", id));

        // checking data correctness
        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Getting CarDTOs list")
    public void getCarDTOs() throws Exception {

        // sending GET request to "/api/cars"
        ResultActions response = mockMvc.perform(get("/api/cars"));

        // checking data correctness
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].registrationNumber", is("XYZ5678")))
                .andExpect(jsonPath("$[1].model.brand", is("BrandB")))
                .andExpect(jsonPath("$[1].model.engine.fuelType", is("Diesel")))
                .andExpect(jsonPath("$[1].model.gearbox.type", is("CVT")));
    }
}
