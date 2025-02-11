package pl.myproject.car_rental_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.myproject.car_rental_api.dto.AddRentDTO;
import pl.myproject.car_rental_api.service.RentService;

@RestController
@RequestMapping("/api/rents")
public class RentController {

    private final RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping
    public ResponseEntity<AddRentDTO> addRent(@RequestBody AddRentDTO addRentDTO) {

        AddRentDTO newRentDTO = rentService.addRent(addRentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(newRentDTO);
    }
}
