package pl.myproject.car_rental_api.unit.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.myproject.car_rental_api.controller.CarController;
import pl.myproject.car_rental_api.dto.CarDTO;
import pl.myproject.car_rental_api.dto.EngineDTO;
import pl.myproject.car_rental_api.dto.GearboxDTO;
import pl.myproject.car_rental_api.dto.ModelDTO;
import pl.myproject.car_rental_api.service.CarService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;


@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @Test
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
