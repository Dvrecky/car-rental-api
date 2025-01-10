package pl.myproject.car_rental_api.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.myproject.car_rental_api.dto.CarDTO;
import pl.myproject.car_rental_api.service.CarService;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService){
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<CarDTO>> getCarsWithDetails() {

        List<CarDTO> carDTOS = carService.getAllCarsWithDetails();

        return ResponseEntity.ok(carDTOS);
    }
}
