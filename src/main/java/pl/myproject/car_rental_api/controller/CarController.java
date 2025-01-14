package pl.myproject.car_rental_api.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable int id) {

        CarDTO carDTO = carService.getCarByIdWithDetails(id);

        return ResponseEntity.ok(carDTO);
    }

    @PostMapping
    public ResponseEntity<CarDTO> addCarWithDetails(@RequestBody CarDTO carDTO) {

        carDTO.setId(0);

        CarDTO savedCarDTO = carService.saveCar(carDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCarDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCarById(@PathVariable int id) {
        carService.deleteCarById(id);

        return ResponseEntity.ok("Car with ID: " + id + " removed successfully");
    }
}
