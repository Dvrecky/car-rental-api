package pl.myproject.car_rental_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myproject.car_rental_api.dto.AddReservationDTO;
import pl.myproject.car_rental_api.dto.ReservationDTO;
import pl.myproject.car_rental_api.dto.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> addReservation(@RequestBody AddReservationDTO addReservationDTO) {

        addReservationDTO.setId(0);

        ReservationDTO savedReservation = reservationService.addReservation(addReservationDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
    }

    @PatchMapping("/{id}/period")
    public ResponseEntity<ReservationDTO> changeReservationPeriod(@RequestBody UpdateReservationDateDTO updateReservationDateDTO, @PathVariable("id") long reservationId) {

        // handover new reservation period and reservation id
        ReservationDTO reservationDTO = reservationService.updateReservationPeriod(updateReservationDateDTO, reservationId);
        return ResponseEntity.ok(reservationDTO);
    }
}
