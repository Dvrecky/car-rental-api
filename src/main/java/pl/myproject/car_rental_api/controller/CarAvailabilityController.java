package pl.myproject.car_rental_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.myproject.car_rental_api.dto.StatusDTO;
import pl.myproject.car_rental_api.service.CarAvailabilityService;

import java.util.List;

@RestController
@RequestMapping("/api/car-availability")
public class CarAvailabilityController {

    private final CarAvailabilityService carAvailabilityService;

    public CarAvailabilityController(CarAvailabilityService carAvailabilityService) {
        this.carAvailabilityService = carAvailabilityService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<StatusDTO>> getCarAvailability(@PathVariable long id) {

        List<StatusDTO> carAvailabilityList = carAvailabilityService.getCarAvailabilityList(id);

        return ResponseEntity.ok(carAvailabilityList);
    }
}
