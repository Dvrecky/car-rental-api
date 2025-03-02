package pl.myproject.car_rental_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myproject.car_rental_api.dto.rent.AddRentDTO;
import pl.myproject.car_rental_api.dto.rent.RentDTO;
import pl.myproject.car_rental_api.dto.rent.UpdateRentDTO;
import pl.myproject.car_rental_api.projection.ClientRentBaseView;
import pl.myproject.car_rental_api.service.RentService;

import java.util.List;

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

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newRentDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RentDTO> updateRent(@PathVariable long id, @RequestBody UpdateRentDTO updateRentDTO) {

        updateRentDTO.setId(id);
        RentDTO updatedRentDTO = rentService.updateRent(updateRentDTO);

        return ResponseEntity.ok(updatedRentDTO);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ClientRentBaseView>> getReservationsByClientId(@PathVariable("id") long clientId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rentService.getRentsByClientId(clientId));
    }
}
