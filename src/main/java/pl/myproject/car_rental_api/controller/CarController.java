package pl.myproject.car_rental_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myproject.car_rental_api.dto.car.CarBaseInfoDTO;
import pl.myproject.car_rental_api.dto.car.CarDetailsDTO;
import pl.myproject.car_rental_api.dto.car.CarListViewDTO;
import pl.myproject.car_rental_api.dto.car.CarSummaryInfoDTO;
import pl.myproject.car_rental_api.service.CarService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    @GetMapping("/details")
    public ResponseEntity<List<CarDetailsDTO>> getCarListWithDetails() {
        return ResponseEntity.ok(carService.getAllCarsWithDetails());
    }

    @GetMapping("/base-info")
    public ResponseEntity<List<CarBaseInfoDTO>> getCarListSummary() {
        return ResponseEntity.ok(carService.getCarsBaseView());
    }

    @GetMapping("/list-view")
    public ResponseEntity<List<CarListViewDTO>> getCarListView() {
        return ResponseEntity.ok(carService.getCarListView());
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<CarDetailsDTO> getCarWithDetailsById(@PathVariable int id) {
        return ResponseEntity.ok(carService.getCarDTOWithDetailsById(id));
    }

    @GetMapping("/{id}/overview")
    public ResponseEntity<CarSummaryInfoDTO> getCarSummaryById(@PathVariable int id) {
        return ResponseEntity.ok(carService.getCarSummaryById(id));
    }

    @PostMapping
    public ResponseEntity<CarDetailsDTO> addCarWithDetails(@RequestBody CarDetailsDTO carDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.saveCar(carDTO));
    }

    @PutMapping
    public ResponseEntity<CarDetailsDTO> updateCar(@RequestBody CarDetailsDTO carDTO) {
        return ResponseEntity.ok(carService.updateCar(carDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCarById(@PathVariable int id) {

        carService.deleteCarById(id);

        return ResponseEntity.ok("Car with ID: " + id + " removed successfully");
    }
}
