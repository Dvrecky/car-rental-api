package pl.myproject.car_rental_api.integration.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.myproject.car_rental_api.dto.CarDTO;
import pl.myproject.car_rental_api.service.CarService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    private CarService carService;

    @Test
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
