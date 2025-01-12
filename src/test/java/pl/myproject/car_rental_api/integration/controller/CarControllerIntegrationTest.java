package pl.myproject.car_rental_api.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
public class CarControllerIntegrationTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void getCarDTOs() throws Exception {
//
//        ResultActions response = mockMvc.perform(get("/api/cars"));
//
//        response.andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.size()", is(3)))
//                .andExpect(jsonPath("$[1].id", is(2)))
//                .andExpect(jsonPath("$[1].registrationNumber", is("XYZ5678")))
//                .andExpect(jsonPath("$[1].model.brand", is("BrandB")))
//                .andExpect(jsonPath("$[1].model.engine.fuelType", is("Diesel")))
//                .andExpect(jsonPath("$[1].model.gearbox.type", is("CVT")));
//
//
//    }
}
