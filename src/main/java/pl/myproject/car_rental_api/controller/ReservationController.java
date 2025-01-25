package pl.myproject.car_rental_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myproject.car_rental_api.dto.ReservationDTO;
import pl.myproject.car_rental_api.dto.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.entity.Reservation;
import pl.myproject.car_rental_api.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> addReservation(@RequestBody Reservation reservation) {
        Reservation savedReservation = reservationService.addReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
    }

    @PatchMapping("/{id}/period")
    public ResponseEntity<ReservationDTO> changeReservationPeriod(@RequestBody UpdateReservationDateDTO updateReservationDateDTO, @PathVariable long id) {

        ReservationDTO reservationDTO = reservationService.updateReservationPeriod(updateReservationDateDTO, id);

        return ResponseEntity.ok(reservationDTO);
    }
}
